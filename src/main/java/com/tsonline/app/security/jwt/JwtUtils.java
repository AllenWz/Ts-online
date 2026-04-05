package com.tsonline.app.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	// Key generation
	public Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	// Token generation
	public String generateTokenFromUsername(UserDetails userDetails) {
		String username = userDetails.getUsername();
		
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(key())
				.compact();
	}
	
	// Getting JWT token from header
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			logger.debug("Authorization Header: {}", bearerToken);
			return bearerToken.substring(7); // Remove the "Bearer " prefix and return only token
		}
		
		return null;
	}
	
	// Token validation
	public boolean validateJwtToken(String authToken) {
		try {
			logger.debug("Token Validation Start...");
			Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(authToken);
			
			return true;
				
		} catch(MalformedJwtException ex) {
			logger.error("Invalid JWT token: {}", ex.getMessage());
		} catch(ExpiredJwtException ex) {
			logger.error("JWT token is expired: {}", ex.getMessage());
		} catch(UnsupportedJwtException ex) {
			logger.error("JWT token is unsupported: {}", ex.getMessage());
		} catch(IllegalArgumentException ex) {
			logger.error("JWT claims string is empty: {}", ex.getMessage());
		}
		return false;
	}
	
	// Getting username from JWT token
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
}
