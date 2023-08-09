package com.example.userservice.security.expressions;

import com.example.userservice.model.User;
import com.example.userservice.security.JwtUser;
import com.example.userservice.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;
    private HttpServletRequest request;

    private TokenService tokenService;

    public CustomMethodSecurityExpressionRoot(
            final Authentication authentication
    ) {
        super(authentication);
    }

    public boolean canAccessUser(final UUID userId) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (!authentication.isAuthenticated()) {
            return false;
        }
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        JwtUser user = (JwtUser) authentication
                .getPrincipal();
        return user.getId().equals(userId)
                || hasAnyRole(User.Role.ROLE_ADMIN);
    }

    public boolean canAccessUser(final String username) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (!authentication.isAuthenticated()) {
            return false;
        }
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        JwtUser user = (JwtUser) authentication
                .getPrincipal();
        return user.getUsername().equals(username)
                || hasAnyRole(User.Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(final User.Role... roles) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (!authentication.isAuthenticated()) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (User.Role role : roles) {
            if (authorities.contains(new SimpleGrantedAuthority(role.name()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getThis() {
        return target;
    }

}
