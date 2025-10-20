package com.bank.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_templates")
public class PaymentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 128)
    private String userId;

    @Column(name = "alias", length = 128)
    private String alias;

    @Column(name = "bank_code", length = 32)
    private String bankCode;

    @Column(name = "account_number", length = 64)
    private String accountNumber;

    @Column(name = "recipient_name", length = 255)
    private String recipientName;

    // getters/setters...
}
