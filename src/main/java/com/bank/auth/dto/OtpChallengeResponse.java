package com.bank.auth.dto;

public class OtpChallengeResponse {
    private String challengeId;
    private String channel;
    public OtpChallengeResponse(){}
    public OtpChallengeResponse(String challengeId, String channel){ this.challengeId = challengeId; this.channel = channel; }
    public String getChallengeId(){ return challengeId; }
    public void setChallengeId(String challengeId){ this.challengeId = challengeId; }
    public String getChannel(){ return channel; }
    public void setChannel(String channel){ this.channel = channel; }
}
