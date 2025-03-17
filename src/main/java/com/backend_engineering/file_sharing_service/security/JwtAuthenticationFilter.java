package com.backend_engineering.file_sharing_service.security;

import com.backend_engineering.file_sharing_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationFilter extends BaseAuthenticationFilter {

    public JwtAuthenticationFilter(UserService userService) {
        super(userService);
    }

    @Override protected UserDetails extractCredentials(HttpServletRequest httpServletRequest) {
        return User.withUsername("dummy-user")
                .build();
    }
}
