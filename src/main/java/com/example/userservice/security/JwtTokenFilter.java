package com.example.userservice.security;

import com.example.userservice.model.authorization.TokenType;
import com.example.userservice.model.exception.NoTokenException;
import com.example.userservice.model.exception.ResourceNotFoundException;
import com.example.userservice.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String token = tokenService.resolve((HttpServletRequest) req);
            if (tokenService.validate(token, TokenType.ACCESS)) {
                Authentication auth = tokenService.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (NoTokenException | ResourceNotFoundException ignored) {
        }
        filterChain.doFilter(req, res);
    }

}
