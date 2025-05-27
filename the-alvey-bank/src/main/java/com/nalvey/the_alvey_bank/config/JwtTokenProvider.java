package com.nalvey.the_alvey_bank.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// JWT = JSON Web Token

@Component
public class JwtTokenProvider{
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // retrieves username of logged in user
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder() 
            .setSubject(username)
            .setIssuedAt(currentDate)
            .setExpiration(expireDate)
            .signWith(key()) // digital signature
            .compact(); // convert built token into string
    }

    // Create secret key to sign & verify JWTs
    private Key key() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Get username from secret key token
    public String getUserName(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject(); // the subject is the username
    }

    // Validate generated token from previous methods
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
            return true; // parse through the token and return true of parsed successfully
        } catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new RuntimeException(e);
    }
}
}
