package com.example.xpresspaymentapi.configuration.security;

import com.example.xpresspaymentapi.model.dto.user.AuthenticationToken;
import com.example.xpresspaymentapi.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secreteKey;

    @Value("${application.security.jwt.expiration}")
    private Long expiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long expiration_refresh;

    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public AuthenticationToken generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getContact().getEmailAddress());
    }

    private AuthenticationToken createToken(Map<String, Object> claims, String emailAddress) {

        String accessToken = buildToken(claims, emailAddress, expiration);
        String refreshToken = generateRefreshToken(emailAddress, expiration_refresh);

        return AuthenticationToken.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public String generateRefreshToken(String emailAddress, Long expiresAt) {
        return buildToken(new HashMap<>(), emailAddress, expiresAt);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, Long expiration) {
        System.out.println(expiration);
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(subject).setIssuer("xpresspayments")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512).compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
       byte[] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
