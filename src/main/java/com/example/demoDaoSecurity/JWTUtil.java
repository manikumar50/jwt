package com.example.demoDaoSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil
{
    // token generation
    static final int EXTIME = 1000 * 60 * 60 * 24; // 1 day
    static final byte KEY[] = "mcY8WBenIQEZeQwcDmo1Nisq6NGc9Xolxh2IMpr4SiM=".getBytes(StandardCharsets.UTF_8);
    static final Key SECRETKEY = Keys.hmacShaKeyFor(KEY);

    public String generateToken(String username,String role)
    {
        return Jwts.builder()
                .claim("username", username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXTIME))
                .signWith(SECRETKEY,  SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(SECRETKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validate(String token)
    {
        try{
            getClaims(token);
            return true;
        }
        catch (JwtException e)
        {
            return false;
        }
    }

    public String extractUsername(String token)
    {
        Claims claims = getClaims(token);
        return (String) claims.get("username");
    }

    public String extractRole(String token)
    {
        Claims claims = getClaims(token);
        return (String) claims.get("role");
    }
}