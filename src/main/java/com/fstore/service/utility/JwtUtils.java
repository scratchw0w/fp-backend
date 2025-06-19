package com.fstore.service.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.Objects;


@Slf4j
@UtilityClass
public class JwtUtils {
    private static final String JWT_COOKIE = "flower-store";
    private static final int JWT_EXPIRATION_MS = 86400000;
    private static final String JWT_SECRET = "MHcCAQEEIERGgdD7j4btzoSOpV8hwTETTWdI1f9qWddBMOe76xMpoAoGCCqGSM49AwEHoUQDQgAEocWhNNPSTW5IeAYJ/QOoWYEqvpUPE1lPHZYQx3GQEQgjZVBRqdD+o2kxnTm8Qf0Bjs2qMpS+RX6KMHPonNtQBA==";

    public static String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, JWT_COOKIE);
        return Objects.nonNull(cookie)
                ? cookie.getValue()
                : null;
    }

    public static ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(JWT_COOKIE, null)
                .path("/api")
                .build();
    }

    public static String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static ResponseCookie generateJwtCookie(String username) {
        return ResponseCookie.from(JWT_COOKIE, generateTokenFromUsername(username))
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    private static String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean isValid(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(authToken);

            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private static Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }
}
