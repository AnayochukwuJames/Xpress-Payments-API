package com.example.xpresspaymentapi.configuration.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security.jwt.secret-key")
public class JwtProperties {
    private long expiration;
    private long refreshTokenExpiration;
}
