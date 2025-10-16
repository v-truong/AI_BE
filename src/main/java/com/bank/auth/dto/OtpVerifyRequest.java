package com.bank.auth.dto;

public class OtpVerifyRequest {
    private String challengeId;
    private String otp;
    public String getChallengeId(){ return challengeId; }
    public void setChallengeId(String challengeId){ this.challengeId = challengeId; }
    public String getOtp(){ return otp; }
    public void setOtp(String otp){ this.otp = otp; }
}
