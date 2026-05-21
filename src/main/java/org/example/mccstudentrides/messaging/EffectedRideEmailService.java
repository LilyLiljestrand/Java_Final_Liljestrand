package org.example.mccstudentrides.messaging;

import org.example.mccstudentrides.data.UserRepository;
import org.example.mccstudentrides.domain.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EffectedRideEmailService {
    private JavaMailSender mailSender;
    private UserRepository userRepo;
    EffectedRideAlert alert;

    public EffectedRideEmailService(JavaMailSender mailSender, UserRepository userRepo){
        this.mailSender = mailSender;
        this.userRepo = userRepo;
    }

    public void sendEffectedRideEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        List<String> bccList = new ArrayList<>();
        userRepo.getUserContactByContact(alert.getUserContact()).stream()
                .map(User::getContact).forEach(bccList::add);
        System.out.println(bccList.toString());
        message.setBcc("managers@myinventoryapp.com");

        String type = "**Effected Ride**";
        message.setText(type + alert.toString() + "\n\nRegards, \nRide Shares System");
        mailSender.send(message);
    }
}
