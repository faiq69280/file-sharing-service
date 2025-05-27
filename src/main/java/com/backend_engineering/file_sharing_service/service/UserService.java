package com.backend_engineering.file_sharing_service.service;

import com.backend_engineering.file_sharing_service.entity.User;
import com.backend_engineering.file_sharing_service.repository.UserRepository;
import com.backend_engineering.file_sharing_service.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;
    JwtUtil  jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateUserAndGenerateJwt(User user) throws NoSuchAlgorithmException {
        return jwtUtil.createJwtToken(requireUserExists(user.getName()));
    }

    public User save(User user) throws IllegalArgumentException{
       return userRepository.save(user)
               .orElseThrow(() -> new IllegalArgumentException("Couldn't save the user"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = requireUserExists(username);
       return org.springframework.security.core.userdetails.User.withUsername(user.getName())
               .authorities(user.getRoles().toArray(String[]::new))
               .password(user.getPassword())
               .build();
    }


    private User requireUserExists(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("No username[%s] found".formatted(username)));
    }
}
