package com.hokhanh.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hokhanh.common.jwt.JwtPropertyConstants;

@Service
public class JwtService {
	
	public String generateAccessToken(UUID authId, UUID userId, String roleName) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", "access");
		claims.put("userId", userId);
		claims.put("roleName", roleName);
		return buildToken(claims, authId, JwtPropertyConstants.ACCESS_TOKEN_EXPIRATION);
	}

	public String generateRefreshToken(UUID authId) {
		Map<String, Object> claims = new HashMap<>();
	    claims.put("type", "refresh");
	    return buildToken(claims, authId, JwtPropertyConstants.REFRESH_TOKEN_EXPIRATION);
	}
	
	private String buildToken(Map<String, Object> extraClaims, UUID authId, long expiration) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(authId.toString())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Date extractExpiration(String token) {
	    try {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignInKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getExpiration();
	    } catch (ExpiredJwtException e) {
	        return e.getClaims().getExpiration(); // vẫn có thể lấy được thời gian hết hạn
	    } catch (Exception e) {
	        return null; // token sai hoặc lỗi khác
	    }
	}
	
	public boolean isRefreshTokenType(String token) {
	    try {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(getSignInKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        String type = claims.get("type", String.class);
	        return "refresh".equals(type);
	    } catch (Exception e) {
	        return false; // token không hợp lệ hoặc không có "type"
	    }
	}
	
	public String extractSubject(String token) {
	    try {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignInKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    } catch (ExpiredJwtException e) {
	        return e.getClaims().getSubject();
	    } catch (Exception e) {
	        return null;
	    }
	}

	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JwtPropertyConstants.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
