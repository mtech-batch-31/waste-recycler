package com.mtech.recycler.config;

import com.mtech.recycler.constant.CommonConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.function.Function;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMinutes}")
    private int jwtExpirationInMinutes;

    public String generateToken(String userName, String role) {
        DateTime now = Instant.now().toDateTime();
        DateTime expiryDate = now.plusMinutes(jwtExpirationInMinutes);

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(now.toDate())
                .setExpiration(expiryDate.toDate())
                .claim(CommonConstant.JwtKey.ROLE, role)
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String getRoleFromJWT(String token) {
        return extractClaim(token, claims -> claims.get(CommonConstant.JwtKey.ROLE).toString());
    }

    public boolean validateToken(String authToken) {
        try {
            SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(authToken);

            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException ex) {
            log.error("Error at validateToken: " + ex.getMessage());
        }
        return false;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
