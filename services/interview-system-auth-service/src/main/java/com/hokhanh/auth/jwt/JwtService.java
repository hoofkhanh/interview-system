package com.hokhanh.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.hokhanh.common.jwt.JwtPropertyConstants;


public class JwtService {
	
	public String generateToken(UUID authId, UUID userId, String roleName) {
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
	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JwtPropertyConstants.SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
