package com.bank.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "devices", indexes = {@Index(columnList = "device_id"), @Index(columnList = "user_id")})
public class Device {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String platform;
    private String model;
    private Instant boundAt;

    public Device(){
        this.id = UUID.randomUUID().toString();
        this.boundAt = Instant.now();
    }

    // getters/setters
    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getDeviceId(){ return deviceId; }
    public void setDeviceId(String deviceId){ this.deviceId = deviceId; }
    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }
    public String getPlatform(){ return platform; }
    public void setPlatform(String platform){ this.platform = platform; }
    public String getModel(){ return model; }
    public void setModel(String model){ this.model = model; }
    public Instant getBoundAt(){ return boundAt; }
    public void setBoundAt(Instant boundAt){ this.boundAt = boundAt; }
}
