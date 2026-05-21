package org.example.mccstudentrides.security;

import org.example.mccstudentrides.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRegistrationForm {
    private String username;
    private String password;
    private String fullname;
    private String contact;
    private String email;
    private String campus;
    private int studentId;
    private String address;
    private String city;
    private String state;

    public User toUser(PasswordEncoder passwordEncoder){
        return new User(username, passwordEncoder.encode(password), fullname, contact, email, campus, studentId, address, city, state);
    }
}
