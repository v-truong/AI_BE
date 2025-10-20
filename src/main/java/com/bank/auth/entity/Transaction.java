package com.bank.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_a", indexes = {@Index(columnList = "account_id"), @Index(columnList = "timestamp")})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store account id as string (no FK); use string to match requirement but store id value
    @Column(name = "account_id", length = 64, nullable = false)
    private String accountId;

    @Column(name = "type", length = 16)
    private String type; // DEBIT or CREDIT

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "timestamp", length = 64)
    private String timestamp; // ISO 8601 string

    public Transaction(){}

    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getAccountId(){ return accountId; }
    public void setAccountId(String accountId){ this.accountId = accountId; }

    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }

    public Double getAmount(){ return amount; }
    public void setAmount(Double amount){ this.amount = amount; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }

    public String getTimestamp(){ return timestamp; }
    public void setTimestamp(String timestamp){ this.timestamp = timestamp; }
}
