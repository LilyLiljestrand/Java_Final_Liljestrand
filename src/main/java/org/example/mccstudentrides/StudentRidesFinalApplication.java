package org.example.mccstudentrides;

import org.example.mccstudentrides.api.Token;
import org.example.mccstudentrides.api.TokenRepository;
import org.example.mccstudentrides.data.UserRepository;
import org.example.mccstudentrides.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentRidesFinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentRidesFinalApplication.class, args);
    }
    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository){
        return args -> {
            userRepository.save(new User("lhartman2", passwordEncoder.encode("admin123"), "Lucas Hartman",
                    "531-622-2332", "lhartman2@mccneb.edu", "Fort Omaha", "123456", "5473 Smith St.", "Omaha", "NE", "DRIVER"));
            tokenRepository.save(new Token(userRepository.findByUsername("lhartman2").getUserId(), "aaaaaa"));
        };
    }

}
