package com.bank.auth.service;

import com.bank.auth.dto.DeviceBindRequest;
import com.bank.auth.dto.DeviceResponse;
import com.bank.auth.entity.Device;
import com.bank.auth.entity.User;
import com.bank.auth.repository.DeviceRepository;
import com.bank.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private static final int MAX_DEVICES = 3;

    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository){
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DeviceResponse bindDevice(DeviceBindRequest req){
        // ensure user exists
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        long count = deviceRepository.countByUserId(user.getId());
        if(count >= MAX_DEVICES){
            List<Device> devices = deviceRepository.findByUserId(user.getId());
            devices.sort((a,b) -> a.getBoundAt().compareTo(b.getBoundAt()));
            Device toRemove = devices.get(0);
            deviceRepository.delete(toRemove);
        }
        Device d = new Device();
        d.setDeviceId(req.getDeviceId());
        d.setPlatform(req.getPlatform());
        d.setModel(req.getModel());
        d.setUserId(user.getId());
        deviceRepository.save(d);
        return new DeviceResponse(d.getDeviceId(), d.getUserId());
    }

    public List<DeviceResponse> listDevices(String userId){
        return deviceRepository.findByUserId(userId).stream()
                .map(d -> new DeviceResponse(d.getDeviceId(), d.getUserId()))
                .collect(Collectors.toList());
    }

    public void deleteDevice(String id){
        Device d = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        deviceRepository.delete(d);
    }
}
