package com.backend_engineering.file_sharing_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Entity
@Table
public class File extends BaseEntity {
    @Column
    private String fileName;

    @Column
    private String originalFileName;

    @Column
    private String fileType;

    @JsonBackReference(value = "user-reference")
    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User parentUser;

    public File() {}


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public User getParentUser() {
        return parentUser;
    }

    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
