package com.yc.rtu.netcustommaster.systemInfo.controller;

import com.yc.rtu.netcustommaster.systemInfo.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/system-info")
@RequiredArgsConstructor
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    @GetMapping("/stream")
    public SseEmitter streamSystemInfo() {
        SseEmitter emitter = new SseEmitter();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleAtFixedRate(() -> {
            try {
                Map<String, Object> cpuUsage = systemInfoService.getCpuUsage();
                Map<String, Object> memoryUsage = systemInfoService.getMemoryUsage();
                Map<String, Object> internetSpeed = systemInfoService.getInternetSpeed();

                String message = String.format(
                        "{ \"cpuUsage\": \"%s\", \"memoryUsage\": \"%s\", \"internetSpeed\": \"%s\" }",
                        cpuUsage.get("cpuUsage"), memoryUsage.get("memoryUsage"), "100Mps"
                );

                emitter.send(SseEmitter.event().data(message));

            } catch (IOException e) {
                emitter.completeWithError(e);
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
