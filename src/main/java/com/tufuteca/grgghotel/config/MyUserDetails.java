package com.tufuteca.grgghotel.config;

import com.tufuteca.grgghotel.model.users.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class MyUserDetails implements UserDetails {

    private Users user;

    public MyUserDetails(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user != null && user.getPersonal() != null && "Администратор".equals(user.getPersonal().getPost())) {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public String getUsername() {return user.getUsername();}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}



}
