package com.myerp.device.controller;

import com.myerp.device.entity.Device;
import com.myerp.device.entity.DeviceStatus;
import com.myerp.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    
    @Autowired
    private DeviceService deviceService;
    
    /**
     * Lista todos os dispositivos
     */
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }
    
    /**
     * Busca dispositivo por ID
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable String deviceId) {
        Device device = deviceService.findByDeviceId(deviceId);
        
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(device);
    }
    
    /**
     * Captura dados de um dispositivo
     */
    @PostMapping("/{deviceId}/capture")
    public ResponseEntity<String> captureFromDevice(@PathVariable String deviceId) {
        String capturedData = deviceService.captureFromDevice(deviceId);
        
        if (capturedData == null) {
            return ResponseEntity.badRequest().body("Falha na captura do dispositivo: " + deviceId);
        }
        
        return ResponseEntity.ok(capturedData);
    }
    
    /**
     * Adiciona novo dispositivo
     */
    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        Device savedDevice = deviceService.addDevice(device);
        return ResponseEntity.ok(savedDevice);
    }
    
    /**
     * Atualiza status do dispositivo
     */
    @PutMapping("/{deviceId}/status")
    public ResponseEntity<String> updateDeviceStatus(@PathVariable String deviceId, 
                                                   @RequestParam DeviceStatus status) {
        deviceService.updateDeviceStatus(deviceId, status);
        return ResponseEntity.ok("Status atualizado para: " + status);
    }
}