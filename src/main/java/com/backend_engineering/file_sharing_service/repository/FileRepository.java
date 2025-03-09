package com.backend_engineering.file_sharing_service.repository;

import com.backend_engineering.file_sharing_service.entity.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, String> {
  public File save(File file);
}
