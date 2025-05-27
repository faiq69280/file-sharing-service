package com.backend_engineering.file_sharing_service.security;

import com.backend_engineering.file_sharing_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationFilter extends BaseAuthenticationFilter {
    @Autowired
    JwtUtil jwtUtil;
    UserService userService;

    public JwtAuthenticationFilter(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    protected UserDetails extractCredentials(HttpServletRequest httpServletRequest) {
        String jwtToken = extractToken(httpServletRequest);
        String userName = (String) jwtUtil.parseClaim(jwtToken, "sub");
        return userService.loadUserByUsername(userName);
    }

    private String extractToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if(!bearerToken.trim().startsWith("Bearer")) { throw new IllegalArgumentException("Bearer token in Auth Header!"); }
        return bearerToken.trim().replace("Bearer", StringUtils.EMPTY).trim();
    }
}
