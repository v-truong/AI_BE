package com.bank.auth.dto;

import java.util.Map;

public class RegisterRequest {
    private String phone;
    private String email;
    private String password;
    private Map<String,Object> profile;
    private String ekycToken;
    public String getPhone(){ return phone; }
    public void setPhone(String phone){ this.phone = phone; }
    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
    public Map<String,Object> getProfile(){ return profile; }
    public void setProfile(Map<String,Object> profile){ this.profile = profile; }
    public String getEkycToken(){ return ekycToken; }
    public void setEkycToken(String ekycToken){ this.ekycToken = ekycToken; }
}
