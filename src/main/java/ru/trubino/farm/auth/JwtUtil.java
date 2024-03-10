package ru.trubino.farm.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;
    @Value("${app.security.jwt.refresh-token-expiration}")
    public int refreshTokenExpiration;
    @Value("${app.security.jwt.access-token-expiration}")
    public int accessTokenExpiration;

    private Key signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getSubject(String token) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String generateToken(UserDetails userDetails, int expiration){
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        return generateToken(userDetails, refreshTokenExpiration);
    }

    public String generateAccessToken(UserDetails userDetails){
        return generateToken(userDetails, accessTokenExpiration);
    }

}
