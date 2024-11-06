package com.yc.rtu.netcustommaster.systemInfo.controller;

import com.yc.rtu.netcustommaster.systemInfo.dto.response.SpeedTestCliResponseDto;
import com.yc.rtu.netcustommaster.systemInfo.service.SystemInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.yc.rtu.netcustommaster.util.SseEmitterUtil.createEmitterWithMessageProvider;

@RestController
@RequestMapping("/api/v1/state")
@RequiredArgsConstructor
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    @Operation(summary = "SSE")
    @GetMapping("/resource")
    public SseEmitter streamSystemInfo() {
        return createEmitterWithMessageProvider(() -> systemInfoService.getSystemInfo().toJson());
    }

    @GetMapping("/speed")
    public ResponseEntity<SpeedTestCliResponseDto> speedTestCli() {
        return ResponseEntity.ok().body(systemInfoService.getSpeedTestCli());
    }
}