package ru.trubino.farm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;
    @Value("${app.security.jwt.refresh-token-expiration}")
    public int refreshTokenExpiration;
    @Value("${app.security.jwt.access-token-expiration}")
    public int accessTokenExpiration;

    private Key signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String extractUsername(String string){
        String BEARER_PREFIX = "Bearer ";
        String token;

        if(string.startsWith(BEARER_PREFIX))
            token = string.substring(BEARER_PREFIX.length());
        else
            token = string;

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

    public Boolean isTokenValid(String token){
        Date expirationDate = Jwts.parserBuilder().setSigningKey(signingKey()).build()
                .parseClaimsJws(token).getBody().getExpiration();

        return new Date().before(expirationDate);
    }




    /*public Boolean validateToken(String token){
        return true;
    }*/

}
