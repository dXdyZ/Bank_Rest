package com.example.bank_rest_test_task.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    public final String username;
    public final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final Boolean accountEnable;
    private final Boolean accountLocked;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Boolean accountEnable, Boolean accountLocked) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountEnable = accountEnable;
        this.accountLocked = accountLocked;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return accountEnable;
    }
}
