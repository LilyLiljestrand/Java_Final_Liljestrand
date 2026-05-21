package org.example.mccstudentrides.web;

import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.domain.Passenger;
import org.example.mccstudentrides.messaging.PassengerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    private final PassengerService passengerService;
    private final RideRepository rideRepo;

    public PassengerController(PassengerService passengerService, RideRepository rideRepo) {
        this.passengerService = passengerService;
        this.rideRepo = rideRepo;
    }

    @GetMapping("/add/{id}")
    public String showPassengerForm(@PathVariable int id, Model model) {
        model.addAttribute("ride", rideRepo.findById(id).orElse(null));
        model.addAttribute("passenger", new Passenger());
        return "passengerForm";
    }

    @PostMapping("/add/{id}")
    public String processPassengerForm(@PathVariable int id, Passenger passenger) {
        passengerService.addPassenger(id, passenger);
        return "redirect:/current/view/" + id;
    }
}

