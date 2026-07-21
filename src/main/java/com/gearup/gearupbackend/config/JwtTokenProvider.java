package com.gearup.gearupbackend.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component // 1. Added this so Spring can inject it
public class JwtTokenProvider {

    @Value("${app.jwt-secret-key}")
    private String secretKey;

    @Value("${app.jwt-expiration-ms}")
    private long expMiliSecs;

    public String generateToken(Authentication authentication){
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + expMiliSecs);

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token); // Use parseSignedClaims for JWS
            return true;
        } catch (Exception ex) {
            // In a real app, you'd log specific exceptions like:
            // MalformedJwtException, ExpiredJwtException, UnsupportedJwtException
            return false;
        }
    }
}