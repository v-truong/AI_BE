package com.bank.auth.controller;

import com.bank.auth.dto.*;
import com.bank.auth.service.AuthService;
import com.bank.auth.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final DeviceService deviceService;

    public AuthController(AuthService authService, DeviceService deviceService){
        this.authService = authService;
        this.deviceService = deviceService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/login/otp")
    public ResponseEntity<OtpChallengeResponse> requestOtp(@RequestBody OtpLoginRequest req){
        return ResponseEntity.ok(authService.requestOtp(req));
    }

    @PostMapping("/login/otp/verify")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpVerifyRequest req){
        return ResponseEntity.ok(authService.verifyOtp(req));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest req){
        return ResponseEntity.ok(authService.refresh(req));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/device/bind")
    public ResponseEntity<DeviceResponse> bindDevice(@RequestBody DeviceBindRequest req){
        return ResponseEntity.ok(deviceService.bindDevice(req));
    }

    @GetMapping("/device")
    public ResponseEntity<List<DeviceResponse>> listDevices(@RequestParam("userId") String userId){
        return ResponseEntity.ok(deviceService.listDevices(userId));
    }

    @DeleteMapping("/device/{id}")
    public ResponseEntity<GenericResponse> deleteDevice(@PathVariable("id") String id){
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(new GenericResponse(true));
    }
}
