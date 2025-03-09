package com.backend_engineering.file_sharing_service.repository;

import com.backend_engineering.file_sharing_service.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByName(String userName);
}

