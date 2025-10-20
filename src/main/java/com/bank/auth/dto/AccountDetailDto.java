package com.bank.auth.dto;

public class AccountDetailDto {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private String currency;
    private Double balance;
    private String updatedAt;
    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getAccountNumber(){ return accountNumber; }
    public void setAccountNumber(String accountNumber){ this.accountNumber = accountNumber; }
    public String getOwnerName(){ return ownerName; }
    public void setOwnerName(String ownerName){ this.ownerName = ownerName; }
    public String getCurrency(){ return currency; }
    public void setCurrency(String currency){ this.currency = currency; }
    public Double getBalance(){ return balance; }
    public void setBalance(Double balance){ this.balance = balance; }
    public String getUpdatedAt(){ return updatedAt; }
    public void setUpdatedAt(String updatedAt){ this.updatedAt = updatedAt; }
}