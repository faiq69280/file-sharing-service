package com.backend_engineering.file_sharing_service.controller;

import com.backend_engineering.file_sharing_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

}
