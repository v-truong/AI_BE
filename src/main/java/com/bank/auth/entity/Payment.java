package com.bank.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments", indexes = {
        @Index(columnList = "from_account_id"),
        @Index(columnList = "to_account_number"),
        @Index(columnList = "status")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_account_id", nullable = false)
    private String fromAccountId;

    @Column(name = "to_account_id")
    private String toAccountId; // nội bộ

    @Column(name = "to_bank_code", length = 16)
    private String toBankCode;  // external

    @Column(name = "to_account_number", length = 64)
    private String toAccountNumber;

    @Column(name = "to_name", length = 255)
    private String toName;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status", length = 64)
    private String status; // PENDING_2FA, SUCCESS, FAILED

    @Column(name = "otp", length = 10)
    private String otp;

    @Column(name = "created_at", length = 64)
    private String createdAt;

    // getters/setters...
}
