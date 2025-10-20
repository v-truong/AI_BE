package com.bank.auth.dto;

public class BalanceDto {
    private Double balance;
    private String currency;
    private String lastUpdated;

    public BalanceDto(){}
    public BalanceDto(Double balance, String currency, String lastUpdated){
        this.balance = balance; this.currency = currency; this.lastUpdated = lastUpdated;
    }
    public Double getBalance(){ return balance; }
    public void setBalance(Double balance){ this.balance = balance; }
    public String getCurrency(){ return currency; }
    public void setCurrency(String currency){ this.currency = currency; }
    public String getLastUpdated(){ return lastUpdated; }
    public void setLastUpdated(String lastUpdated){ this.lastUpdated = lastUpdated; }
}
