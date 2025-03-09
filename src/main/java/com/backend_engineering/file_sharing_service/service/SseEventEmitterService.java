package com.backend_engineering.file_sharing_service.service;

import com.backend_engineering.file_sharing_service.dto.EventDTO;
import com.backend_engineering.file_sharing_service.dto.FileStatusDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEventEmitterService implements ConsumerNotifierService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String eventId) {
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10 min timeout
        emitters.put(eventId, emitter);

        emitter.onCompletion(() -> emitters.remove(eventId));
        emitter.onTimeout(() -> emitters.remove(eventId));

        return emitter;
    }

    @Override
    public void notify(EventDTO notificationObject) {
        SseEmitter emitter = emitters.get(notificationObject.eventId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(notificationObject.eventType()).data(notificationObject));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } finally {
                emitters.remove(notificationObject.eventId());
            }
        }
    }
}
