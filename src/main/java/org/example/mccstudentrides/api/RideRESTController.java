package org.example.mccstudentrides.api;

import org.example.mccstudentrides.domain.Passenger;
import org.example.mccstudentrides.messaging.PassengerService;
import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.data.UserRepository;
import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.RideType;
import org.example.mccstudentrides.domain.User;
import org.example.mccstudentrides.web.RideProps;
import org.example.mccstudentrides.messaging.EffectedRide;
import org.example.mccstudentrides.messaging.EffectedRideAlert;
import org.example.mccstudentrides.messaging.EffectedRideRepository;
import org.example.mccstudentrides.messaging.KafkaEffectedRideProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/rides", produces="application/json")
public class RideRESTController {
    private RideRepository rideRepo;
    private RideProps rideProps;
    private TokenRepository tokenRepo;
    private UserRepository userRepo;
    private EffectedRideRepository effectRideRepo;
    private KafkaEffectedRideProducer kafkaEffectRideProducer;
    private PassengerService passengerService;


    public RideRESTController(RideRepository rideRepo, RideProps rideProps, TokenRepository tokenRepo,
                              UserRepository userRepo, EffectedRideRepository effectRideRepo, KafkaEffectedRideProducer kafkaEffectRideProducer,
                              PassengerService passengerService){
        this.rideRepo = rideRepo;
        this.rideProps = rideProps;
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.effectRideRepo = effectRideRepo;
        this.kafkaEffectRideProducer = kafkaEffectRideProducer;
        this.passengerService = passengerService;
    }

    public boolean isAuthorized(HttpServletRequest request){
        String authToken = request.getHeader("Authorization");
        return authToken != null && tokenRepo.existsByToken(authToken);
    }

    public boolean isUserAuthorized(HttpServletRequest request, int id){
        boolean isAuthorized = isAuthorized(request);
        if(isAuthorized){
            User tokenUser = userRepo.getUserById(tokenRepo.getUserIdByToken(request.getHeader("Authorization")).getUserId());
            isAuthorized = tokenUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DRIVER")) ||
                    tokenUser.equals(rideRepo.findById(id).orElse(null).getUser());
        }
        return isAuthorized;
    }

    @GetMapping()
    public Iterable<RideDTO> getRides(@RequestParam Optional<Integer> page,
                                           @RequestParam Optional<Integer> pageSize,
                                           HttpServletRequest request){
        boolean isAuthorized = isAuthorized(request);
        int p = page.isPresent() ? page.get() : 0;
        int ps = pageSize.isPresent() ? pageSize.get() : rideProps.getPageSize();
        Pageable pageable = PageRequest.of(p, ps, Sort.by("id"));
        Page<Ride> db = rideRepo.findAll(pageable);
        Iterable<RideDTO> rides = db.stream().map(RideDTO::new).toList();
        if(!isAuthorized){
            rides.forEach(a -> a.setUser(new UserDTO()));
        }
        return rides;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable int id, HttpServletRequest request){
        boolean isAuthorized = isAuthorized(request);
        Optional<Ride> ride = rideRepo.findById(id);
        if(ride.isPresent()){
            RideDTO rideData = new RideDTO(ride.get());
            if(!isAuthorized){
                rideData.setUser(new UserDTO());
            }
            return new ResponseEntity<>(rideData, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping("/filter")
    public Iterable<RideDTO> getRideByFilter(@RequestParam String type,
                                             @RequestParam Optional<Integer> page,
                                             @RequestParam Optional<Integer> pageSize,
                                             HttpServletRequest request){
        boolean isAuthorized = isAuthorized(request);
        int p = page.isPresent() ? page.get() : 0;
        int ps = pageSize.isPresent() ? pageSize.get() : rideProps.getPageSize();
        Pageable pageable = PageRequest.of(p, ps, Sort.by("id"));
        RideType filter = RideType.valueOf(type.toUpperCase());
        Iterable<RideDTO> rides = rideRepo.findRidesByCampusContainingIgnoreCase(filter, pageable)
                .stream().map(RideDTO::new).toList();
        if(!isAuthorized){
            rides.forEach(a -> a.setUser(new UserDTO()));
        }
        return rides;


    }

    @GetMapping("/search")
    public Iterable<RideDTO> getRideBySearchCity(@RequestParam String city,
                                                   @RequestParam Optional<Integer> page,
                                                   @RequestParam Optional<Integer> pageSize,
                                                   HttpServletRequest request){
        boolean isAuthorized = isAuthorized(request);
        int p = page.isPresent() ? page.get() : 0;
        int ps = pageSize.isPresent() ? pageSize.get() : rideProps.getPageSize();
        Pageable pageable = PageRequest.of(p, ps, Sort.by("id"));
        Iterable<RideDTO> rides = rideRepo.findRidesByIdContainingIgnoreCase(id, pageable)
                .stream().map(RideDTO::new).toList();
        if(!isAuthorized){
            rides.forEach(a -> a.setUser(new UserDTO()));
        }
        return rides;
    }

    @PostMapping(consumes="application/json")
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO, HttpServletRequest request){
        boolean isAuthorized = isAuthorized(request);
        if(!isAuthorized){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Ride ride = new Ride(rideDTO);
        ride.setUser(userRepo.findById(tokenRepo.getUserIdByToken(request.getHeader("Authorization"))
                .getUserId()).orElse(null));
        rideRepo.save(ride);
        return new ResponseEntity<>(new RideDTO(ride), HttpStatus.CREATED);
    }

    @PutMapping(consumes="application/json")
    public ResponseEntity<RideDTO> putRide(@RequestBody RideDTO rideDTO, HttpServletRequest request){
        boolean isAuthorized = isUserAuthorized(request, rideDTO.getId());
        if(!isAuthorized){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Ride ride = new Ride(rideDTO);
        ride.setUser(rideRepo.findById(rideDTO.getId()).get().getUser());
        rideRepo.save(ride);
        return new ResponseEntity<>(new RideDTO(ride), HttpStatus.OK);
    }

    @PatchMapping(consumes="application/json")
    public ResponseEntity<RideDTO> patchRide(@RequestBody RideDTO patch, HttpServletRequest request){
        boolean isAuthorized = isUserAuthorized(request, patch.getId());
        if(!isAuthorized){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Ride> rCheck = rideRepo.findById(patch.getId());
        if(rCheck.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Ride ride = rCheck.get();
        if(patch.getName() != null){
            ride.setName(patch.getName());
        }
        if(patch.getContact() != null){
            ride.setContact(patch.getContact());
        }
        if(patch.getCampus() != null){
            ride.setCampus(patch.getCampus());
        }
        if(patch.getSeats() != 0){
            ride.setSeats(patch.getSeats());
        }
        if(patch.getCarModel() != null){
            ride.setCarModel(patch.getCarModel());
        }
        if(patch.getClassTime() != 0){
            ride.setClassTime(patch.getClassTime());
        }
        if(patch.getAddress() != null){
            ride.setAddress(patch.getAddress());
        }

        if(patch.getCarPhoto() != null){
            ride.setCarPhoto(patch.getCarPhoto());
        }

        rideRepo.save(ride);
        return new ResponseEntity<>(new RideDTO(ride), HttpStatus.OK);
    }

    //adding the passengets
    /**@PostMapping("/{id}/passengers")
    public ResponseEntity<RideDTO> addPassengerToRide(@PathVariable int id, @RequestBody Passenger passenger, HttpServletRequest request) {

        if (!isAuthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Passenger saved = passengerService.addPassenger(id, passenger);
            return new ReponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }*/


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable int id, HttpServletRequest request){
        boolean isAuthorized = isUserAuthorized(request, id);
        if (isAuthorized){
            try{
                rideRepo.deleteById(id);
            }catch(EmptyResultDataAccessException e){
                System.out.println(e);
            }
        }
    }

    @PostMapping("effect/{id}")
    public ResponseEntity<EffectedRideAlert> EffectRide(@PathVariable int id, HttpServletRequest request){
        boolean isAuthorized = isUserAuthorized(request, id);
        if (!isAuthorized){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Ride> ride = rideRepo.findById(id);
        if (ride.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Ride r = ride.get();
        if (effectRideRepo.existsByRideId(r.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        EffectedRide effectRide = new EffectedRide();
        //effectRide.setRide(r);

        effectRideRepo.save(effectRide);

        EffectedRideAlert alert = new EffectedRideAlert();

        kafkaEffectRideProducer.sendEffectedRideAlert(alert);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
