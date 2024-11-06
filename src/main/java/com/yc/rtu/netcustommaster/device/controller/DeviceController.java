package com.yc.rtu.netcustommaster.device.controller;

import com.yc.rtu.netcustommaster.device.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.yc.rtu.netcustommaster.util.SseEmitterUtil.createEmitterWithMessageProvider;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @Operation(summary = "SSE")
    @GetMapping
    public SseEmitter streamTrafficData() {
        return createEmitterWithMessageProvider(()-> deviceService.getTrafficData().toJson());
    }
}
