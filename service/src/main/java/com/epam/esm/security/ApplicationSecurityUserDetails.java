package com.epam.esm.security;

import com.epam.esm.entities.Role;
import com.epam.esm.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ApplicationSecurityUserDetails implements UserDetails {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    private final String username;
    private final String password;
    private final GrantedAuthority authority;

    public ApplicationSecurityUserDetails(User user) {
        username = user.getUsername();
        password = user.getPassword();
        authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    public String getAuthority() {
        return authority.getAuthority();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
