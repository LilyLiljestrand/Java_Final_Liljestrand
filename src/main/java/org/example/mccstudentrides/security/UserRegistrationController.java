package org.example.mccstudentrides.security;

import org.example.mccstudentrides.api.Token;
import org.example.mccstudentrides.api.TokenRepository;
import org.example.mccstudentrides.domain.User;
import org.example.mccstudentrides.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepo;

    public UserRegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder, TokenRepository tokenRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
    }

    @GetMapping("/register")
    public String registrationForm(){
        return "userRegistrationForm";
    }

    @PostMapping("/register")
    public String processRegistration(UserRegistrationForm form){
        User u = userRepo.save(form.toUser(passwordEncoder));
        Token token = new Token(u.getUserId(), randomStringGenerator());
        System.out.println("User ID: " + u.getUserId() + " - Token: " + token.getToken());
        tokenRepo.save(token);
        return "redirect:/login";
    }

    private String randomStringGenerator(){
        int leftLimit = 48;
        int rightLimit = 122;
        int targetSuperLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i ->(i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
