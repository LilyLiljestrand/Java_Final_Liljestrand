package org.example.mccstudentrides.web;

import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.domain.RideType;
import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegisterRideController {
    private RideRepository rideDB;

    public void registerRideController(RideRepository rideDB){
        this.rideDB = rideDB;
    }

    @ModelAttribute
    public void addRideTypeModel(Model model){
        model.addAttribute("RideType", RideType.values());
    }

    @ModelAttribute
    public Ride ride(){
        return new Ride();
    }

    @GetMapping
    public String registerForm(){
        return "rideRegistrationForm";
    }

    @PostMapping
    public String processRegister(@Valid Ride ride, Errors errors, @AuthenticationPrincipal User user){
        if(errors.hasErrors()){
            return "rideRegistrationForm";
        }

        ride.setUser(user);
        ride.getCarPhoto().setImageName(ride.getName() + "." + ride.getCarPhoto().getEncoding());
        rideDB.save(ride);
        return "redirect:/view/current" + ride.getId();
    }
}
