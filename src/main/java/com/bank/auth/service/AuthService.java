package com.bank.auth.service;

import com.bank.auth.dto.*;
import com.bank.auth.entity.User;
import com.bank.auth.entity.RefreshToken;
import com.bank.auth.repository.RefreshTokenRepository;
import com.bank.auth.repository.UserRepository;
import com.bank.auth.security.JwtService;
import com.bank.auth.service.otp.OtpService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpService otpService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtService jwtService,
                       BCryptPasswordEncoder passwordEncoder,
                       OtpService otpService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    public AuthResponse login(LoginRequest req) {
        Optional<User> uOpt = userRepository.findByPhoneOrEmail(req.getIdentity(), req.getIdentity());
        if (uOpt.isEmpty()) throw new RuntimeException("Invalid credentials");
        User user = uOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) throw new RuntimeException("Invalid credentials");
        String access = jwtService.generateAccessToken(user.getId());
        String refresh = jwtService.generateRefreshToken(user.getId());
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUserId(user.getId());
        rt.setExpiresAt(Instant.now().plus(jwtService.getRefreshExpirySeconds(), ChronoUnit.SECONDS));
        refreshTokenRepository.save(rt);
        return new AuthResponse(access, refresh, jwtService.getAccessExpirySeconds());
    }

    public OtpChallengeResponse requestOtp(OtpLoginRequest req) {
        OtpData otpData = otpService.createChallenge(req.getIdentity());
        return new OtpChallengeResponse(otpData.getChallengeId(), "sms", otpData.getOtp());
    }

    public AuthResponse verifyOtp(OtpVerifyRequest req) {
        String userId = otpService.verifyChallenge(req.getChallengeId(), req.getOtp());
        System.out.println("OTP verified for userId: " + userId);
        Optional<User> uOpt = userRepository.findById(userId);
         if (uOpt.isEmpty()){uOpt = userRepository.findByPhoneOrEmail(userId, userId);}
        if (uOpt.isEmpty()){ throw new RuntimeException("User not found for OTP");
    }
        User user = uOpt.get();
        String access = jwtService.generateAccessToken(user.getId());
        String refresh = jwtService.generateRefreshToken(user.getId());
        RefreshToken rt = new RefreshToken();
        rt.setToken(refresh);
        rt.setUserId(user.getId());
        rt.setExpiresAt(Instant.now().plus(jwtService.getRefreshExpirySeconds(), ChronoUnit.SECONDS));
        refreshTokenRepository.save(rt);
        return new AuthResponse(access, refresh, jwtService.getAccessExpirySeconds());
    }

    public AuthResponse refresh(RefreshTokenRequest req) {
        RefreshToken rt = refreshTokenRepository.findByToken(req.getRefreshToken()).orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (rt.getExpiresAt().isBefore(Instant.now())) throw new RuntimeException("Refresh token expired");
        String access = jwtService.generateAccessToken(rt.getUserId());
        String refresh = jwtService.generateRefreshToken(rt.getUserId());
        rt.setToken(refresh);
        rt.setExpiresAt(Instant.now().plus(jwtService.getRefreshExpirySeconds(), ChronoUnit.SECONDS));
        refreshTokenRepository.save(rt);
        return new AuthResponse(access, refresh, jwtService.getAccessExpirySeconds());
    }

    public RegisterResponse register(RegisterRequest req) {
        User u = new User();
        u.setPhone(req.getPhone());
        u.setEmail(req.getEmail());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
        return new RegisterResponse(u.getId());
    }
}
