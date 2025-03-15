package com.backend_engineering.file_sharing_service.service;

import com.backend_engineering.file_sharing_service.constants.FileTransferStatus;
import com.backend_engineering.file_sharing_service.dto.EventDTO;
import com.backend_engineering.file_sharing_service.dto.FileStatusDTO;
import com.backend_engineering.file_sharing_service.entity.File;
import com.backend_engineering.file_sharing_service.entity.User;
import com.backend_engineering.file_sharing_service.repository.FileRepository;
import com.backend_engineering.file_sharing_service.repository.UserRepository;
import io.minio.*;
import io.minio.http.Method;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileStorageService {

    @Value("${minio.bucket-name}")
    private String bucketName;

    MinioClient minioClient;

    FileRepository fileRepository;

    UserRepository userRepository;

    public FileStorageService(MinioClient minioClient, FileRepository fileRepository, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public FileStatusDTO upload(String userName, MultipartFile multiPartFile, ConsumerNotifierService consumerNotifierService, String eventId) throws Exception {

        User fetchedUser = userRepository.findByName(userName)
                .orElse(new User());

        fetchedUser.setName(userName);
        uploadFileAsync(multiPartFile, eventId, fetchedUser, consumerNotifierService);
        return new FileStatusDTO(multiPartFile.getOriginalFilename(), new Date(System.currentTimeMillis()), FileTransferStatus.IN_PROGRESS);
    }

    @Transactional(rollbackOn = Exception.class)
    @Async
    private void uploadFileAsync(MultipartFile multiPartFile, String eventId, User fetchedUser, ConsumerNotifierService consumerNotifierService) throws Exception {

        InputStream inputStream = multiPartFile.getInputStream();


        String fileName = UUID.randomUUID() + "_"
                + System.currentTimeMillis() + "_"
                + multiPartFile.getOriginalFilename();


        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
                .stream(inputStream, multiPartFile.getSize(), -1)
                .contentType(multiPartFile.getContentType())
                .build());

        File file = new File();
        file.setFileName(fileName);
        file.setFileType(multiPartFile.getContentType());
        file.setParentUser(fetchedUser);
        file.setOriginalFileName(multiPartFile.getOriginalFilename());
        Optional.ofNullable(fileRepository.save(file)).orElseThrow(() -> new IllegalStateException("Save failure"));

        consumerNotifierService.notify(new EventDTO(eventId, new FileStatusDTO(fileName,
                new Date(System.currentTimeMillis()),
                FileTransferStatus.COMPLETE), "uploadStatus"));
    }

    private String getPreSignedUrl(String fileName, int expiry) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)  // Change to PUT for uploads
                        .bucket(bucketName)
                        .object(fileName)  // File must exist in the bucket
                        .expiry(expiry, TimeUnit.SECONDS)  // URL expiration time
                        .build()
        );
    }

}
