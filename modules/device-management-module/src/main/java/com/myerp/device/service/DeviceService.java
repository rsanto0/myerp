package com.myerp.device.service;

import com.myerp.device.entity.Device;
import com.myerp.device.entity.DeviceStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Serviço responsável pela gestão de dispositivos
 */
@Service
public class DeviceService {
    
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    
    // Simulação de dispositivos em memória (futuramente usar repository)
    private List<Device> devices = new ArrayList<>();
    
    public DeviceService() {
        initializeDefaultDevices();
    }
    
    /**
     * Captura dados de um dispositivo específico
     */
    public String captureFromDevice(String deviceId) {
        Device device = findByDeviceId(deviceId);
        
        if (device == null) {
            logger.warn("Dispositivo não encontrado: {}", deviceId);
            return null;
        }
        
        if (device.getStatus() != DeviceStatus.ACTIVE) {
            logger.warn("Dispositivo {} não está ativo: {}", deviceId, device.getStatus());
            return null;
        }
        
        // Simula captura de dados
        String capturedData = simulateCapture(device);
        
        // Atualiza último ping
        device.setLastPing(LocalDateTime.now());
        
        logger.info("Dados capturados do dispositivo {}: {}", deviceId, device.getName());
        
        return capturedData;
    }
    
    /**
     * Lista todos os dispositivos
     */
    public List<Device> getAllDevices() {
        return new ArrayList<>(devices);
    }
    
    /**
     * Busca dispositivo por ID
     */
    public Device findByDeviceId(String deviceId) {
        return devices.stream()
                     .filter(d -> d.getDeviceId().equals(deviceId))
                     .findFirst()
                     .orElse(null);
    }
    
    /**
     * Adiciona novo dispositivo
     */
    public Device addDevice(Device device) {
        device.setId((long) (devices.size() + 1));
        device.setCreatedAt(LocalDateTime.now());
        devices.add(device);
        
        logger.info("Dispositivo adicionado: {} - {}", device.getDeviceId(), device.getName());
        
        return device;
    }
    
    /**
     * Atualiza status do dispositivo
     */
    public void updateDeviceStatus(String deviceId, DeviceStatus status) {
        Device device = findByDeviceId(deviceId);
        if (device != null) {
            device.setStatus(status);
            logger.info("Status do dispositivo {} atualizado para: {}", deviceId, status);
        }
    }
    
    /**
     * Simula captura de dados baseado no tipo de dispositivo
     */
    private String simulateCapture(Device device) {
        switch (device.getType()) {
            case CAMERA:
            case FACE_RECOGNITION_CAMERA:
                return "image_data_" + System.currentTimeMillis() + ".jpg";
            case FINGERPRINT_SCANNER:
                return "fingerprint_data_" + System.currentTimeMillis();
            case RFID_READER:
                return "rfid_tag_" + System.currentTimeMillis();
            default:
                return "generic_data_" + System.currentTimeMillis();
        }
    }
    
    /**
     * Inicializa dispositivos padrão para demonstração
     */
    private void initializeDefaultDevices() {
        devices.add(new Device("CAM001", "Câmera Entrada Principal", 
                              com.myerp.device.entity.DeviceType.FACE_RECOGNITION_CAMERA, "Entrada Principal"));
        devices.add(new Device("CAM002", "Câmera RH", 
                              com.myerp.device.entity.DeviceType.CAMERA, "Departamento RH"));
        devices.add(new Device("BIO001", "Scanner Biométrico Recepção", 
                              com.myerp.device.entity.DeviceType.FINGERPRINT_SCANNER, "Recepção"));
        
        logger.info("Dispositivos padrão inicializados: {} dispositivos", devices.size());
    }
}