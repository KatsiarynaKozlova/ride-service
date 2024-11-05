package com.software.modsen.rideservice.security;

import com.software.modsen.rideservice.util.SecurityConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class User implements UserDetails, OAuth2User {

    private UUID id;
    private String username;
    private String phone;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                SecurityConstants.USER_FIELD_ID, id,
                SecurityConstants.USER_FIELD_USERNAME, username,
                SecurityConstants.USER_FIELD_EMAIL, email,
                SecurityConstants.USER_FIELD_PHONE, phone
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return null;
    }
}
