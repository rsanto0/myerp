package com.myerp.device.repository;

import com.myerp.device.entity.Device;
import com.myerp.device.entity.DeviceStatus;
import com.myerp.device.entity.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    Optional<Device> findByDeviceId(String deviceId);
    
    List<Device> findByStatus(DeviceStatus status);
    
    List<Device> findByType(DeviceType type);
    
    List<Device> findByLocation(String location);
    
    @Query("SELECT d FROM Device d WHERE d.status = 'ACTIVE'")
    List<Device> findActiveDevices();
    
    @Query("SELECT d FROM Device d WHERE d.lastPing IS NULL OR d.lastPing < CURRENT_TIMESTAMP - INTERVAL '5 minutes'")
    List<Device> findOfflineDevices();
}