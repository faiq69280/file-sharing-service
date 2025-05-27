package com.backend_engineering.file_sharing_service.security;

import com.backend_engineering.file_sharing_service.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;

    public String createJwtToken(User user) throws NoSuchAlgorithmException {
        return Jwts.builder()
                .setSubject(user.getName())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuedAt(new Date())
                .setExpiration(getJwtExpiration(10))
                .compact();
    }

    private Date getJwtExpiration(long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }

    public Object parseClaim(String jwt, String claimKey) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJwt(jwt).getBody();

        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("JWT expired!");
        }
        return claims.get(claimKey);
    }
}

