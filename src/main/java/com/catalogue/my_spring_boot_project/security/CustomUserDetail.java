package com.catalogue.my_spring_boot_project.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail implements UserDetails {

    private Long id;
    private String emailOrUid;
    private String password;
    private String avatarUrl;
    private Collection<? extends GrantedAuthority> authorities;

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
        return emailOrUid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setUsername(String emailOrUid) {
        this.emailOrUid = emailOrUid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 没有过期控制的话直接返回 true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 没有锁定控制
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 密码不过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 账户可用
    }

}
