package org.pacs.userloginregistrationapi.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class NonceGenerator {

    public String generateNonceWithDateSeed() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String seed = formatter.format(currentDate);
        return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
    }
}
