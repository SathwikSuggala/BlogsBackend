package com.Blogs.Backend.BlogsBackend.Security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MyUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    String grantedAuthority;
    String password;
    String userName;

    public MyUserDetails(String grantedAuthority, String userName, String password){

        //System.out.println(grantedAuthority);
        this.grantedAuthority = grantedAuthority;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println(grantedAuthority);
        return Collections.singleton(new SimpleGrantedAuthority(grantedAuthority));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
