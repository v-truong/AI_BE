package com.bank.auth.controller;

import com.bank.auth.dto.*;
import com.bank.auth.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/internal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> createInternal(@RequestBody PaymentRequest req) {
        return ResponseEntity.ok(paymentService.createInternal(req));
    }

    @PostMapping("/external")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> createExternal(@RequestBody PaymentRequest req) {
        return ResponseEntity.ok(paymentService.createExternal(req));
    }

    @PostMapping("/{paymentId}/confirm")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> confirm(@PathVariable Long paymentId, @RequestBody ConfirmPaymentRequest req) {
        return ResponseEntity.ok(paymentService.confirmPayment(paymentId, req));
    }
}
