package com.backend_engineering.file_sharing_service.controller;

import com.backend_engineering.file_sharing_service.service.ConsumerNotifierService;
import com.backend_engineering.file_sharing_service.service.FileStorageService;
import com.backend_engineering.file_sharing_service.service.SseEventEmitterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("sse")
public class SseController extends AbstractController {

    SseEventEmitterService sseEventEmitterService;
    public SseController(FileStorageService fileStorageService, SseEventEmitterService sseEventEmitterService) {
        super(fileStorageService);
        this.sseEventEmitterService = sseEventEmitterService;
    }

    @Override
    public ConsumerNotifierService getConsumerNotifierService() {
        return sseEventEmitterService;
    }

    @GetMapping("/fetchEventId")
    public ResponseEntity<String> fetchEventId() {
        return new ResponseEntity<>(UUID.randomUUID().toString(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/subscribe/{eventId}")
    public SseEmitter subscribe(@PathVariable("eventId") String eventId) {
        return sseEventEmitterService.createEmitter(eventId);
    }
}
