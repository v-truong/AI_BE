package com.bank.auth.service;

import com.bank.auth.dto.*;
import com.bank.auth.entity.Payment;
import com.bank.auth.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;

    public PaymentService(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public PaymentResponse createInternal(PaymentRequest req) {
        Payment p = new Payment();
        p.setFromAccountId(req.getFromAccountId());
        p.setToAccountId(req.getToAccountId());
        p.setAmount(req.getAmount());
        p.setDescription(req.getDescription());
        p.setStatus("PENDING_2FA");
        p.setOtp(generateOtp());
        p.setCreatedAt(Instant.now().toString());
        paymentRepo.save(p);
        return new PaymentResponse(p.getId().toString(), p.getStatus());
    }

    public PaymentResponse createExternal(PaymentRequest req) {
        Payment p = new Payment();
        p.setFromAccountId(req.getFromAccountId());
        p.setToBankCode(req.getToBankCode());
        p.setToAccountNumber(req.getToAccountNumber());
        p.setToName(req.getToName());
        p.setAmount(req.getAmount());
        p.setDescription(req.getDescription());
        p.setStatus("PENDING_2FA");
        p.setOtp(generateOtp());
        p.setCreatedAt(Instant.now().toString());
        paymentRepo.save(p);
        return new PaymentResponse(p.getId().toString(), p.getStatus());
    }

    public PaymentResponse confirmPayment(Long id, ConfirmPaymentRequest req) {
        Optional<Payment> p = paymentRepo.findById(id);
        if (p.isEmpty()) throw new RuntimeException("Payment not found");

        Payment payment = p.get();
        if (!payment.getOtp().equals(req.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        payment.setStatus("SUCCESS");
        paymentRepo.save(payment);
        return new PaymentResponse(payment.getId().toString(), payment.getStatus());
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random()*900000)+100000);
    }
}
