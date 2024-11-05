package com.yc.rtu.netcustommaster.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SseEmitterUtil {
    public static SseEmitter createEmitterWithMessageProvider(MessageProvider messageProvider) {
        SseEmitter emitter = new SseEmitter(120_000L);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleAtFixedRate(() -> {
            try {
                String message = messageProvider.getMessage();
                if (message != null) {
                    emitter.send(SseEmitter.event().data(message));
                }
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

    // 메시지를 제공하는 인터페이스
    public interface MessageProvider {
        String getMessage();
    }
}
