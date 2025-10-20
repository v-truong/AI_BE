package com.bank.auth.dto;

public class OtpData {
    private String challengeId;
    private String otp;

    public OtpData() {}

    public OtpData(String challengeId, String otp) {
        this.challengeId = challengeId;
        this.otp = otp;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
}
