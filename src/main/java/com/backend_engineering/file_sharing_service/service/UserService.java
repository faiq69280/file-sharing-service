package com.backend_engineering.file_sharing_service.service;

import com.backend_engineering.file_sharing_service.entity.User;
import com.backend_engineering.file_sharing_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByName(username)
               .orElseThrow(() -> new IllegalArgumentException("No username[%s] found".formatted(username)));

       return org.springframework.security.core.userdetails.User.withUsername(user.getName())
               .authorities(user.getRoles().toArray(String[]::new))
               .password(user.getPassword())
               .build();
    }
}
