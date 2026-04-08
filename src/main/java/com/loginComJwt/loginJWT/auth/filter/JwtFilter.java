package com.loginComJwt.loginJWT.auth.filter;

import com.loginComJwt.loginJWT.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtService jwtService;
    public JwtFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);

            return;
        }
        logger.info("Header: {}", request.getHeader("Authorization"));


        String token = authHeader.substring(7);
        logger.info("Token recebebido: {}", token);
        logger.info("Token valido? {}", jwtService.validaToken(token));
        if(jwtService.validaToken(token)){
            String email = jwtService.extrairEmail(token);
            logger.info("Token válido", jwtService.validaToken(token));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Usuário autenticado: {}", email);
        }

        filterChain.doFilter(request, response);
    }
}
