package com.bank.auth.repository;

import com.bank.auth.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByUserId(String userId);
    Optional<Device> findByUserIdAndDeviceId(String userId, String deviceId);
    long countByUserId(String userId);
}
