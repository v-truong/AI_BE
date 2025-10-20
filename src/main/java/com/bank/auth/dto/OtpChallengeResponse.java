package com.bank.auth.dto;

public class OtpChallengeResponse {
    private String challengeId;
    private String channel;
    private String otp;

    public OtpChallengeResponse() {}

    public OtpChallengeResponse(String challengeId, String channel, String otp) {
        this.challengeId = challengeId;
        this.channel = channel;
        this.otp = otp;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}