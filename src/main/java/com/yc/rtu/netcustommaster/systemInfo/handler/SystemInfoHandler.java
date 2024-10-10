package com.yc.rtu.netcustommaster.systemInfo.handler;

import com.yc.rtu.netcustommaster.systemInfo.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SystemInfoHandler extends TextWebSocketHandler {

    private final SystemInfoService systemInfoService;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                Map<String, Object> cpuUsage = systemInfoService.getCpuUsage();
                Map<String, Object> memoryUsage = systemInfoService.getMemoryUsage();
                Map<String, Object> internetSpeed = systemInfoService.getInternetSpeed();

                String message = String.format(
                        "{ \"cpuUsage\": \"%s\", \"memoryUsage\": \"%s\", \"internetSpeed\": \"%s\" }",
                        cpuUsage.get("cpuUsage"), memoryUsage.get("memoryUsage"),
                        internetSpeed.get("speedTest")
                );

                session.sendMessage(new TextMessage(message));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS); // 5초 간격으로 전송
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        executorService.shutdown();
    }
}
