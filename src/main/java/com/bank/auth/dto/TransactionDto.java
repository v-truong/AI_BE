package com.bank.auth.dto;

public class TransactionDto {
    private Long id;
    private String type;
    private Double amount;
    private String description;
    private String timestamp;

    public TransactionDto(){}
    public TransactionDto(Long id, String type, Double amount, String description, String timestamp){
        this.id = id; this.type = type; this.amount = amount; this.description = description; this.timestamp = timestamp;
    }
    // getters/setters
    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }
    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }
    public Double getAmount(){ return amount; }
    public void setAmount(Double amount){ this.amount = amount; }
    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
    public String getTimestamp(){ return timestamp; }
    public void setTimestamp(String timestamp){ this.timestamp = timestamp; }
}
