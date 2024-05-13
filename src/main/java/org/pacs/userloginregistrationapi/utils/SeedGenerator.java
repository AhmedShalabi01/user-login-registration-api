package org.pacs.userloginregistrationapi.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SeedGenerator {
    public String generateSeed() {
        // Fetch current date and time (in ms)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        return now.format(formatter);
    }
}
