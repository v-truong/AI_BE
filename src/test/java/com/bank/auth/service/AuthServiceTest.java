package com.bank.auth.service;

import com.bank.auth.dto.*;
import com.bank.auth.entity.User;
import com.bank.auth.entity.RefreshToken;
import com.bank.auth.repository.RefreshTokenRepository;
import com.bank.auth.repository.UserRepository;
import com.bank.auth.security.JwtService;
import com.bank.auth.service.otp.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock JwtService jwtService;
    @Mock OtpService otpService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    AuthService authService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, refreshTokenRepository, jwtService, encoder, otpService);
    }

    @Test
    public void testLoginSuccess(){
        User u = new User();
        u.setPhone("+1234567890");
        u.setPasswordHash(encoder.encode("password123"));
        when(userRepository.findByPhoneOrEmail(anyString(), anyString())).thenReturn(Optional.of(u));
        when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(jwtService.getAccessExpirySeconds()).thenReturn(900L);
        when(jwtService.getRefreshExpirySeconds()).thenReturn(1209600L);

        LoginRequest req = new LoginRequest();
        req.setIdentity("+1234567890");
        req.setPassword("password123");
        AuthResponse res = authService.login(req);
        assertNotNull(res);
        assertEquals("access-token", res.getAccessToken());
        assertEquals("refresh-token", res.getRefreshToken());
    }

    @Test
    public void testOtpFlow() {
        when(otpService.createChallenge(anyString())).thenReturn("challenge-1");
        OtpLoginRequest req = new OtpLoginRequest();
        req.setIdentity("+1000");
        var ch = authService.requestOtp(req);
        assertEquals("challenge-1", ch.getChallengeId());
    }

    @Test
    public void testVerifyOtpAndRefresh() {
        when(otpService.verifyChallenge("c1","000000")).thenReturn("user-1");
        User u = new User();
        u.setId("user-1");
        when(userRepository.findById("user-1")).thenReturn(Optional.of(u));
        when(jwtService.generateAccessToken(anyString())).thenReturn("a");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("r");
        when(jwtService.getAccessExpirySeconds()).thenReturn(900L);
        when(jwtService.getRefreshExpirySeconds()).thenReturn(1209600L);

        OtpVerifyRequest vr = new OtpVerifyRequest();
        vr.setChallengeId("c1");
        vr.setOtp("000000");
        AuthResponse ar = authService.verifyOtp(vr);
        assertEquals("a", ar.getAccessToken());

        RefreshToken rt = new RefreshToken();
        rt.setToken("r");
        rt.setUserId("user-1");
        rt.setExpiresAt(Instant.now().plusSeconds(1000));
        when(refreshTokenRepository.findByToken("r")).thenReturn(Optional.of(rt));
        when(jwtService.generateAccessToken(anyString())).thenReturn("a2");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("r2");
        var resp = authService.refresh(new RefreshTokenRequest(){{
            setRefreshToken("r");
        }});
        assertEquals("a2", resp.getAccessToken());
    }
}
