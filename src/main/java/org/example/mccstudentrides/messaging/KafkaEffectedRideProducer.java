package org.example.mccstudentrides.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEffectedRideProducer {
    private KafkaTemplate<String, EffectedRideAlert> kafkaTemplate;


    public KafkaEffectedRideProducer(KafkaTemplate<String, EffectedRideAlert> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEffectedRideAlert(EffectedRideAlert alert){
        kafkaTemplate.send("effect-ride-topic", alert);
    }
}
