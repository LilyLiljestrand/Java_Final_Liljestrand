package org.example.mccstudentrides.web;

import org.example.mccstudentrides.domain.Passenger;
import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.RideType;
import org.example.mccstudentrides.domain.User;
import org.example.mccstudentrides.messaging.EffectedRide;
import org.example.mccstudentrides.messaging.EffectedRideAlert;
import org.example.mccstudentrides.messaging.EffectedRideRepository;
import org.example.mccstudentrides.messaging.KafkaEffectedRideProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/view")
public class RideViewController {
    //
    private RideRepository rideDB;
    private EffectedRideRepository effectRideRepo;
    private KafkaEffectedRideProducer kafkaEffectRideProducer;
    private Passenger passenger;

    public void RideViewController(RideRepository rideDB, EffectedRideRepository effectRideRepo, KafkaEffectedRideProducer kafkaEffectRideProducer, Passenger passenger){
        this.rideDB = rideDB;
        this.effectRideRepo = effectRideRepo;
        this.kafkaEffectRideProducer = kafkaEffectRideProducer;
        this.passenger = passenger;
    }

    @GetMapping("/current/{id}")
    public String viewRide(@PathVariable int id, Model model){
        Optional<Ride> ride = rideDB.findById(id);
        if(ride.isEmpty()){
            return "redirect:/list";
        }
        Ride current = ride.get();
        model.addAttribute("current", current);
        return "viewRide";
    }

    //add passenger to the ride and then subtract from the total number of available seats
     @PostMapping("/add/{id}")
     public String addPassenger(){
        Optional<Ride> ride = rideDB.findById(id);
        if (ride.isEmpty()){
            return "redirect:/list";
        }
        Ride current = ride.get();
        Passenger pass = passenger.get();
        if(current.getUser.equals(user) || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PASSENGER"))){
            //taking seats from Ride and numPassenger from Passenger
        }
     }


    @PostMapping("/delete/{id}")
    public String deleteRide(@PathVariable int id, @AuthenticationPrincipal User user){
        Optional<Ride> ride = rideDB.findById(id);
        if(ride.isEmpty()){
            return "redirect:/list";
        }
        Ride current = ride.get();
        if(current.getUser().equals(user) || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DRIVER"))){
            rideDB.deleteById(id);
        }
        return "redirect:/view/current" + id;
    }

    @PostMapping("/modify/{id}")
    public String modifyRide(@PathVariable int id, Model model, @AuthenticationPrincipal User user){
        Optional<Ride> ride = rideDB.findById(id);
        if(ride.isEmpty()){
            return "redirect:/list";
        }
        Ride current = ride.get();
        if(current.getUser().equals(user) || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DRIVER"))){
            model.addAttribute("RideType", RideType.values());
            model.addAttribute("ride", current);
            return "rideModificationForm";
        }
        return "redirect:/view/current/" + current.getId();
    }

    @PostMapping("/modify")
    @PostAuthorize("hasRole('DRIVER') or ride.user.username == authentication.name")
    public String modifyRide(@Valid Ride ride, Errors errors){
        if(errors.hasErrors()){
            return "rideModificationForm";
        }
        Optional<Ride> r = rideDB.findById(ride.getId());
        Ride lookup = r.get();
        if(lookup.hasImage() && !ride.hasImage()){
            ride.getCarPhoto().setImageName(lookup.getCarPhoto().getImageName());
            ride.getCarPhoto().setImageContent(lookup.getCarPhoto().getImageContent());
        }
        ride.setUser(lookup.getUser());
        rideDB.save(ride);
        return "redirect:/view/current/" + ride.getId();
    }

    @PostMapping("/effected/{id}")
    public String effectedRide(@PathVariable int id, @AuthenticationPrincipal User user){
        Optional<Ride> ride = rideDB.findById(id);
        if (ride.isEmpty()){
            return "redirect:/list/";
        }

        Ride r = ride.get();
        if(!r.getUser().equals(user) && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DRIVER"))){
            return "redirect:/view/current/" + r.getId();
        }
        if(effectRideRepo.existsByRideId(r.getId())){
            return "redirect:/view/current/" + r.getId();
        }

        EffectedRide eRide = new EffectedRide();
        eRide.set(r);

        effectRideRepo.save(eRide);

        EffectedRideAlert alert = new EffectedRideAlert(r.getId(), r.getName(), r.getAddress(), r.getSeats(), null);

        kafkaEffectRideProducer.sendEffectedRideAlert(alert);

        return "redirect:/view/current/" + r.getId();
    }
}
