package com.backend_engineering.file_sharing_service.dto;

import com.backend_engineering.file_sharing_service.constants.FileTransferStatus;

import java.util.Date;

public record FileStatusDTO(String fileName, Date date, FileTransferStatus fileTransferStatus) {}
