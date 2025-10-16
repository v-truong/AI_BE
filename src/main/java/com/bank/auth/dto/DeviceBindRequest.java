package com.bank.auth.dto;

public class DeviceBindRequest {
    private String deviceId;
    private String platform;
    private String model;
    private String userId;
    public String getDeviceId(){ return deviceId; }
    public void setDeviceId(String deviceId){ this.deviceId = deviceId; }
    public String getPlatform(){ return platform; }
    public void setPlatform(String platform){ this.platform = platform; }
    public String getModel(){ return model; }
    public void setModel(String model){ this.model = model; }
    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }
}
