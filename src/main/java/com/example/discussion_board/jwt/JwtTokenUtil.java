package com.example.discussion_board.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

// This class use to create/authenticate token jwt
@Slf4j
@Component
public class JwtTokenUtil {
    private final Key signingKey;
    private final JwtParser jwtParser;

    //Constructor user to create signingKey and JwtParser
    public JwtTokenUtil(
          @Value("${jwt.secret}")
          String jwtSecret
    ) {
        log.info("jwtSecret: {}", jwtSecret);
        // create signingKey by HMAC-SHA algorithms
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        // create JwtParser
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }

    // Generate Token
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(60*60*24)));
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.signingKey)
                .compact();
    }

    // Validate jwt Token
    public boolean validate(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            log.info("subject: {}", claims.getSubject());
            log.info("issuedAt: {}", claims.getIssuedAt());
            log.info("expireAt: {}", claims.getExpiration());
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt provided: {}", e.getMessage());
        }
        return false;
    }

    // Get info from token without authentication
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
