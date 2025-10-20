package com.bank.auth.dto;

public class AccountListItemDto {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private String currency;

    public AccountListItemDto(){}
    public AccountListItemDto(Long id, String accountNumber, String ownerName, String currency){
        this.id = id; this.accountNumber = accountNumber; this.ownerName = ownerName; this.currency = currency;
    }
    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getAccountNumber(){ return accountNumber; }
    public void setAccountNumber(String accountNumber){ this.accountNumber = accountNumber; }
    public String getOwnerName(){ return ownerName; }
    public void setOwnerName(String ownerName){ this.ownerName = ownerName; }
    public String getCurrency(){ return currency; }
    public void setCurrency(String currency){ this.currency = currency; }
}
