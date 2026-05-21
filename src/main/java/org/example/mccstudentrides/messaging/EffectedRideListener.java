package org.example.mccstudentrides.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EffectedRideListener {
    @KafkaListener(topics="effect-ride-topic", groupId="effect-group")
    public void handleEffectedRide(EffectedRideAlert alert){
        System.out.println(alert);
    }
}
