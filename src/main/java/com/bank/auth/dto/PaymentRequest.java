package com.bank.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String fromAccountId;
    private String toAccountId;       // internal
    private String toBankCode;        // external
    private String toAccountNumber;
    private String toName;
    private Double amount;
    private String description;
    // getters/setters...
}
