package com.bank.auth.dto;

public class DeviceResponse {
    private String deviceId;
    private String userId;
    public DeviceResponse(){}
    public DeviceResponse(String deviceId, String userId){ this.deviceId = deviceId; this.userId = userId; }
    public String getDeviceId(){ return deviceId; }
    public void setDeviceId(String deviceId){ this.deviceId = deviceId; }
    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }
}
