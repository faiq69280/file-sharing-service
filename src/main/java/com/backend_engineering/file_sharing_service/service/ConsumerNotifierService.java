package com.backend_engineering.file_sharing_service.service;

import com.backend_engineering.file_sharing_service.dto.EventDTO;
import com.backend_engineering.file_sharing_service.dto.FileStatusDTO;

public interface ConsumerNotifierService {
    public void notify(EventDTO message);
}
