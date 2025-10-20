package com.bank.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_schedules")
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 128)
    private String userId;

    @Column(name = "from_account_id", length = 128)
    private String fromAccountId;

    @Column(name = "to_bank_code", length = 32)
    private String toBankCode;

    @Column(name = "to_account_number", length = 64)
    private String toAccountNumber;

    @Column(name = "to_name", length = 255)
    private String toName;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "recurrence", length = 32)
    private String recurrence; // daily, weekly, monthly

    @Column(name = "next_run", length = 64)
    private String nextRun;

    // getters/setters...
}
