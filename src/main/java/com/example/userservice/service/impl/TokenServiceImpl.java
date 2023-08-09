package com.example.userservice.service.impl;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.Response;
import com.example.userservice.model.authorization.TokenType;
import com.example.userservice.model.exception.AccessDeniedException;
import com.example.userservice.model.exception.NoTokenException;
import com.example.userservice.model.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.TokenService;
import com.example.userservice.service.props.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public String generate(TokenType type, User user) {
        return switch (type) {
            case ACCESS -> createAccessToken(user);
            case REFRESH -> createRefreshToken(user);
            case ACTIVATION -> createActivationToken(user);
            case RESTORE -> createRestoreToken(user);
        };
    }

    private String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("roles", resolveRoles(user.getRoles()));
        claims.put("type", TokenType.ACCESS);
        Instant expirationDate = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(User user) {
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("type", TokenType.REFRESH);
        Instant expirationDate = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createActivationToken(User user) {
        if (user.getStatus() != User.Status.NOT_ACTIVATED) {
            throw new IllegalStateException("User is already activated.");
        }

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("type", TokenType.ACTIVATION);
        Instant expirationDate = Instant.now()
                .plus(jwtProperties.getActivation(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRestoreToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("type", TokenType.RESTORE);
        Instant expirationDate = Instant.now()
                .plus(jwtProperties.getReset(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expirationDate))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Response refresh(String refreshToken) {
        if (!validate(refreshToken, TokenType.REFRESH)) {
            throw new AccessDeniedException();
        }
        UUID userId = getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
        return Response.builder()
                .userId(userId)
                .username(user.getUsername())
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user))
                .build();
    }

    private List<String> resolveRoles(Set<User.Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.security.core.Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(getUsername(token));
        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(userDetails,
                    "",
                    userDetails.getAuthorities());
        }
        return null;
    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public UUID getUserId(String token) {
        return UUID.fromString(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId")
                .toString());
    }

    @Override
    public boolean validate(String token, TokenType tokenType) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date())
                    && claims.getBody().get("type").equals(tokenType.name());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String resolve(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new NoTokenException();
    }

}
