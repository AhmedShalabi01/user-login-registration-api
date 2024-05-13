package org.pacs.userloginregistrationapi.service;

import lombok.RequiredArgsConstructor;
import org.pacs.userloginregistrationapi.documents.NonceTracker;
import org.pacs.userloginregistrationapi.repositories.NonceRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NonceService {

    private final NonceRepository nonceRepository;

    public void generateNonceSequence(String userID, String seed, Integer sequenceLength) {
        // Create nonce list
        List<String> nonceSequence = new ArrayList<>();

        // Start generation
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] seedBytes = seed.getBytes();
        byte[] hash = digest.digest(seedBytes);

        for (int i = 0; i < sequenceLength; i++) {
            nonceSequence.add(bytesToHex(hash));
            hash = digest.digest(hash); // Hash the previous hash to generate the next nonce
        }

        System.out.println(nonceSequence);

        addNonceTracker(userID, nonceSequence);
    }

    private void addNonceTracker(String userID, List<String> nonceSequence) {
        NonceTracker newNonceTracker = new NonceTracker(
                userID,
                nonceSequence,
                0
        );
        nonceRepository.save(newNonceTracker);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}