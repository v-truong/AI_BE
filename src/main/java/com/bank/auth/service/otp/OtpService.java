package com.bank.auth.service.otp;

import org.springframework.stereotype.Service;

import com.bank.auth.dto.OtpData;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    public static class OtpEntry {
        public String userId;
        public String otp;
        public Instant expiresAt;
    }

    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();

    public OtpData createChallenge(String identity){
        OtpData otpData = new OtpData();
        String challengeId = UUID.randomUUID().toString();
        OtpEntry e = new OtpEntry();
        e.userId = identity; // identity used as userId/phone shortcut for demo
        e.otp = String.format("%06d", (int)(Math.random()*900000)+100000);
        e.expiresAt = Instant.now().plusSeconds(120);
        store.put(challengeId, e);
        System.out.println("OTP for challenge " + challengeId + " => " + e.otp);
        otpData.setChallengeId(challengeId);
        otpData.setOtp(e.otp); // for testing/demo purposes
        return otpData;
    }

    public String verifyChallenge(String challengeId, String otp){
        OtpEntry e = store.get(challengeId);
        if(e == null) throw new RuntimeException("Invalid challenge");
        if(e.expiresAt.isBefore(Instant.now())) { store.remove(challengeId); throw new RuntimeException("OTP expired"); }
        if(!e.otp.equals(otp)) throw new RuntimeException("Invalid OTP");
        store.remove(challengeId);
        return e.userId;
    }
}
