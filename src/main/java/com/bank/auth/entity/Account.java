package com.bank.auth.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "account_a", indexes = { @Index(columnList = "user_id"), @Index(columnList = "account_number") })
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store user id as string (no FK)
    @Column(name = "user_id", length = 128, nullable = false)
    private String userId;

    @Column(name = "account_number", unique = true, length = 64, nullable = false)
    private String accountNumber;

    @Column(name = "owner_name", length = 255)
    private String ownerName;

    @Column(name = "currency", length = 8)
    private String currency;

    @Column(name = "balance")
    private Double balance = 0.0;

    // ISO 8601 string timestamps
    @Column(name = "created_at", length = 64)
    private String createdAt;

    @Column(name = "updated_at", length = 64)
    private String updatedAt;

    public Account(){}

    // getters / setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }

    public String getAccountNumber(){ return accountNumber; }
    public void setAccountNumber(String accountNumber){ this.accountNumber = accountNumber; }

    public String getOwnerName(){ return ownerName; }
    public void setOwnerName(String ownerName){ this.ownerName = ownerName; }

    public String getCurrency(){ return currency; }
    public void setCurrency(String currency){ this.currency = currency; }

    public Double getBalance(){ return balance; }
    public void setBalance(Double balance){ this.balance = balance; }

    public String getCreatedAt(){ return createdAt; }
    public void setCreatedAt(String createdAt){ this.createdAt = createdAt; }

    public String getUpdatedAt(){ return updatedAt; }
    public void setUpdatedAt(String updatedAt){ this.updatedAt = updatedAt; }
}
