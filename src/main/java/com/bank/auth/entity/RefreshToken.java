package com.bank.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens", indexes = {@Index(columnList = "token"), @Index(columnList = "user_id")})
public class RefreshToken {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private Instant expiresAt;

    public RefreshToken(){
        this.id = UUID.randomUUID().toString();
    }

    // getters/setters
    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getToken(){ return token; }
    public void setToken(String token){ this.token = token; }
    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }
    public Instant getExpiresAt(){ return expiresAt; }
    public void setExpiresAt(Instant expiresAt){ this.expiresAt = expiresAt; }
}
