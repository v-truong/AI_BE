package com.bank.auth.dto;

public class LoginRequest {
    private String identity;
    private String password;
    public String getIdentity(){ return identity; }
    public void setIdentity(String identity){ this.identity = identity; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
}
