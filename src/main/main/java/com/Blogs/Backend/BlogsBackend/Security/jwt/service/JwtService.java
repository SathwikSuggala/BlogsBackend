package com.Blogs.Backend.BlogsBackend.Security.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtService {
    public String generateToken(String userName, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*240))
                .signWith(getSecKey(), SignatureAlgorithm.HS256).compact();

    }

    public void addTokenToResponse(HttpServletResponse response, String token, String role) {
        Cookie jwtCookie = new Cookie("jwt", token);
        Cookie roleCookie = new Cookie("role", role);
//        jwtCookie.setHttpOnly(true); // Makes the cookie inaccessible to JavaScript
//        roleCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Ensures cookie is sent only over HTTPS
        roleCookie.setSecure(true);
        jwtCookie.setPath("/"); // Make cookie available to all paths
        roleCookie.setPath("/");
        // Set cookie expiration in IST
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime istExpirationTime = ZonedDateTime.now(istZoneId).plusHours(1); // 1 hour from now

        // Convert the expiration time to GMT
        ZonedDateTime gmtExpirationTime = istExpirationTime.withZoneSameInstant(ZoneId.of("GMT"));

        // Set the cookie's expiration in GMT
        jwtCookie.setMaxAge((int) (istExpirationTime.toEpochSecond() - ZonedDateTime.now(istZoneId).toEpochSecond()));
        roleCookie.setMaxAge((int) (istExpirationTime.toEpochSecond() - ZonedDateTime.now(istZoneId).toEpochSecond()));

        response.addCookie(jwtCookie);
        response.addCookie(roleCookie);

    }

    private Key getSecKey() {
        byte[] keybytes = Decoders.BASE64.decode("3273357638792F423F4528482B4D6251655368566D597133743677397A244326");
        return Keys.hmacShaKeyFor(keybytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        boolean flag = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
