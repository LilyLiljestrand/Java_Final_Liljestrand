package org.example.mccstudentrides.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ride")
@Data
public class RideProps {
    private int pageSize = 15;
}
