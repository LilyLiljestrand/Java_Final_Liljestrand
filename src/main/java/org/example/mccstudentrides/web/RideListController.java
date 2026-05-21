package org.example.mccstudentrides.web;

import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.RideType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/list")
public class RideListController {
    private RideRepository rideDB;
    private RideProps rideProps;

    public RideListController(RideRepository rideDB, RideProps rideProps){
        this.rideDB = rideDB;
        this.rideProps = rideProps;
    }

    @ModelAttribute
    public void addRideTypeToModel(Model model){
        model.addAttribute("RideType", RideType.values());
    }

    @GetMapping({"/", "/{currentPage}"})
    public String rideList(Model model, @PathVariable Optional<Integer> currentPage){
        Map<String, Ride> db = new HashMap<>();
        //
        int page = currentPage.orElse(1) - 1;
        Pageable pageable = PageRequest.of(page, rideProps.getPageSize());
        Page<Ride> rides = rideDB.findAll(pageable);

        if(page > rides.getTotalPages()){
            return "redirect:/list/";
        }

        rideDB.findAll().forEach(ride-> db.put(ride.getId(), ride));
        model.addAttribute("rideDB", db);
        model.addAttribute("totalPages", rides.getTotalPages());
        model.addAttribute("currentPage", page + 1);

        model.addAttribute("campus", "list");
        return "rideList";
    }

    @GetMapping({"/filter", "/filter/{currentPage}"})
    public String filterRide(@PathVariable Optional<Integer> currentPage, @RequestParam String filterType, Model model){
        Map<String, Ride> db = new HashMap<>();

        int page = currentPage.orElse(1) - 1;
        Pageable pageable = PageRequest.of(page, rideProps.getPageSize());
        RideType filter = RideType.valueOf(filterType.toUpperCase()); //fixed from assignment 3
        Page<Ride> rides = rideDB.findRidesByCampusContainingIgnoreCase(filter, pageable);

        if(page > rides.getTotalPages()){
            return "redirect:/list/";
        }

        rides.forEach(ride -> db.put(ride.getId(), ride));
        model.addAttribute("rideDB", db);
        model.addAttribute("totalPages", rides.getTotalPages());
        model.addAttribute("currentPage", page + 1);

        model.addAttribute("campus", "filter");
        model.addAttribute("filterType", filterType);
        return "rideList";
    }

    @GetMapping({"/search", "/search/{currentPage}"})
    public String searchRide(@RequestParam String searchCity, Model model, @PathVariable Optional<Integer> currentPage){
        Map<String, Ride> db = new HashMap<>();

        int page = currentPage.orElse(1) - 1;
        Pageable pageable = PageRequest.of(page, rideProps.getPageSize(), Sort.by("id"));
        Page<Ride> rides = rideDB.findRidesByCityContainingIgnoreCase(searchCity, pageable);

        if(page > rides.getTotalPages()){
            return "redirect:/list/";
        }

        rides.forEach(ride -> db.put(ride.getId(), ride));
        model.addAttribute("rideDB", db);
        model.addAttribute("totalPages", rides.getTotalPages());
        model.addAttribute("currentPage", page + 1);

        model.addAttribute("type", "search");
        model.addAttribute("searchCity", searchCity);
        return "rideList";
    }

}
