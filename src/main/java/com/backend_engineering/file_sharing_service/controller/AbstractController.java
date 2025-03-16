package com.backend_engineering.file_sharing_service.controller;

import com.backend_engineering.file_sharing_service.dto.FileStatusDTO;
import com.backend_engineering.file_sharing_service.service.ConsumerNotifierService;
import com.backend_engineering.file_sharing_service.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractController {

    private FileStorageService fileStorageService;
    AbstractController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("upload")
    public ResponseEntity<FileStatusDTO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("eventId") String eventId) throws Exception {
        if (file == null || file.isEmpty()) { throw new IllegalArgumentException("Empty file"); }
        ConsumerNotifierService consumerNotifierService = getConsumerNotifierService();
        String userName = "TEST_USER";
        return new ResponseEntity<>(fileStorageService.upload(userName, file, consumerNotifierService, eventId), HttpStatus.ACCEPTED);
    }

    @GetMapping("download_url")
    public ResponseEntity<String> downloadUrl(@RequestParam("fileUuid") String fileUuid) throws Exception {
        return new ResponseEntity<>(fileStorageService.getDownloadUrl(fileUuid), HttpStatus.OK);
    }

    public abstract ConsumerNotifierService getConsumerNotifierService();
}
