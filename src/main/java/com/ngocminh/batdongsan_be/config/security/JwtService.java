package com.ngocminh.batdongsan_be.config.security;

import com.ngocminh.batdongsan_be.model.Agent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    // Khóa mới đủ 512-bit cho HS512 (được sinh tự động, an toàn)
    private static final String SECRET_KEY =
            "E1mHgT9VdlbS4yJtQivFqA4rKNwM5zU6oRrJt2VQbEcmJpK3wT5qB7dXf9sL1pR8zKcLxG4nF2vA1mT3sY7hC9wD6eR0jH8qP";

    // Giải mã key base64 thành Key object cho JWT
    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Tạo JWT token có chứa thông tin user + plan
    public String generateToken(UserDetails userDetails,
                                LocalDateTime planEndDate,
                                Agent.PlanType planName,
                                String role,
                                UUID userId) {

        JwtBuilder builder = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60)) // 60 ngày
                .signWith(getSignInKey(), SignatureAlgorithm.HS512);

        if (planEndDate != null) {
            builder.claim("planEndDate", planEndDate.toString());
        }

        if (planName != null) {
            builder.claim("planName", planName.name());
        }

        return builder.compact();
    }

    // Kiểm tra token hợp lệ (chưa hết hạn và đúng user)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
