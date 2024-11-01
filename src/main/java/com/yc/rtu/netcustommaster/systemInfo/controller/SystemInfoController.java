package com.yc.rtu.netcustommaster.systemInfo.controller;

import com.yc.rtu.netcustommaster.systemInfo.dto.response.SpeedTestCliResponseDto;
import com.yc.rtu.netcustommaster.systemInfo.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/state")
@RequiredArgsConstructor
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    @GetMapping("/resource")
    public SseEmitter streamSystemInfo() {
        return createSystemInfoEmitter();
    }

    @GetMapping("/speed")
    public ResponseEntity<SpeedTestCliResponseDto> speedTestCli() {
        return ResponseEntity.ok(systemInfoService.getSpeedTestCli());
    }

    private SseEmitter createSystemInfoEmitter() {
        SseEmitter emitter = new SseEmitter(120_000L);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().data(systemInfoService.getSystemInfo().toJson()));
            } catch (IOException e) {
                emitter.completeWithError(e);
                executorService.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS); // 1초마다 데이터 전송

        emitter.onCompletion(executorService::shutdown);
        emitter.onTimeout(() -> {
            emitter.complete();
            executorService.shutdown();
        });

        return emitter;
    }
}