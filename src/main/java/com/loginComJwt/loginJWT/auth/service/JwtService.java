package com.loginComJwt.loginJWT.auth.service;

import com.loginComJwt.loginJWT.auth.filter.JwtFilter;
import com.loginComJwt.loginJWT.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    @Value("${jwt.secret}")
    private String secretKey;

    private Key getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UserModel usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extrairClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extrairEmail(String token){
        return extrairClaims(token).getSubject();
    }

    public Long extrairId(String token){
        return extrairClaims(token).get("id", Long.class);
    }

    public boolean validaToken(String token){
        try {
            extrairClaims(token);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
