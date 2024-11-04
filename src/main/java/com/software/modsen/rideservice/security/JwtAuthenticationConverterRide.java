package com.software.modsen.rideservice.security;

import com.software.modsen.rideservice.util.SecurityConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverterRide implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());

        User user = extractUserInfo(jwt);

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                authorities
        );
    }

    private User extractUserInfo(Jwt jwt) {
        return User.builder()
                .phone(jwt.getClaim(SecurityConstants.USER_FIELD_PHONE))
                .id(UUID.fromString(jwt.getClaim(SecurityConstants.TOKEN_FIELD_ID)))
                .email(jwt.getClaim(SecurityConstants.USER_FIELD_EMAIL))
                .username(jwt.getClaim(SecurityConstants.TOKEN_FIELD_USERNAME))
                .build();
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, List<String>> resourceAccess = jwt.getClaim(SecurityConstants.REALM_ACCESS);
        List<String> resourceRoles;

        if (resourceAccess == null || (resourceRoles = resourceAccess.get(SecurityConstants.ROLES)) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority(SecurityConstants.PREFIX_ROLE + role))
                .collect(Collectors.toList());
    }
}
