package com.yc.rtu.netcustommaster.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SseEmitterUtil {

    // 메시지를 주기적으로 전송하는 메소드
    public static SseEmitter createEmitterWithMessageProvider(MessageProvider messageProvider) {
        SseEmitter emitter = new SseEmitter(120_000L);  // 120초 동안 연결 유지
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        // 1초마다 데이터를 전송
        executorService.scheduleAtFixedRate(() -> {
            try {
                // MessageProvider로부터 메시지 받아오기
                String message = messageProvider.getMessage();
                if (message != null) {
                    emitter.send(SseEmitter.event().data(message));  // 클라이언트로 데이터 전송
                }
            } catch (IOException e) {
                // 예외 발생 시 연결 종료
                emitter.completeWithError(e);
                executorService.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);  // 1초마다 데이터 전송

        // emitter가 완료될 때 executor 종료
        emitter.onCompletion(executorService::shutdown);
        emitter.onTimeout(() -> {
            // 타임아웃 시 연결 종료
            emitter.complete();
            executorService.shutdown();
        });

        return emitter;
    }

    // 메시지를 제공하는 인터페이스
    public interface MessageProvider {
        String getMessage();  // 메시지를 제공하는 메소드
    }

    // MessageProvider의 예시 구현 클래스
    public static class SimpleMessageProvider implements MessageProvider {
        private int counter = 0;

        @Override
        public String getMessage() {
            // 메시지 생성 로직 예시 (여기서는 메시지 번호 증가)
            return "Message number " + (++counter);
        }
    }
}
