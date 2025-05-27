package com.backend_engineering.file_sharing_service.controller;

import com.backend_engineering.file_sharing_service.dto.LoginDTO;
import com.backend_engineering.file_sharing_service.entity.User;
import com.backend_engineering.file_sharing_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws NoSuchAlgorithmException {
        User user = new User();
        user.setName(loginDTO.username());
        user.setPassword(loginDTO.password());
        return new ResponseEntity<>(userService.authenticateUserAndGenerateJwt(user), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

}
