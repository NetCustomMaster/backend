package com.yc.rtu.netcustommaster.systemInfo.controller;

import com.yc.rtu.netcustommaster.systemInfo.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/system-info")
@RequiredArgsConstructor
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    @GetMapping("/cpu")
    public ResponseEntity<Map<String, Object>> getCpuUsage() {
        Map<String, Object> response = systemInfoService.getCpuUsage();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/memory")
    public ResponseEntity<Map<String, Object>> getMemoryUsage() {
        Map<String, Object> response = systemInfoService.getMemoryUsage();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/internet-speed")
    public ResponseEntity<Map<String, Object>> getInternetSpeed() {
        Map<String, Object> response = systemInfoService.getInternetSpeed();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/connected-devices")
    public ResponseEntity<Map<String, Object>> getConnectedDevices() {
        Map<String, Object> response = systemInfoService.getConnectedDevices();
        return ResponseEntity.ok(response);
    }
}
