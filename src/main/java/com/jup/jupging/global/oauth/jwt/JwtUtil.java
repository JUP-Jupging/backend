package com.jup.jupging.global.oauth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	 private final Key key;
	    private final long accessTtl;
	    private final long refreshTtl;

	    public JwtUtil(
	            @Value("${jwt.secret}") String secret,
	            @Value("${jwt.access-ttl-ms:1800000}") long accessTtl,
	            @Value("${jwt.refresh-ttl-ms:1209600000}") long refreshTtl
	    ) {
	        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 32B 이상
	        this.accessTtl = accessTtl;
	        this.refreshTtl = refreshTtl;
	    }

	    /** Access Token 발급 (provider/roles는 필요 시 전달) */
	    public String generateAccessToken(Long memberId, String provider, Collection<String> roles) {
	        Date now = new Date();
	        JwtBuilder b = Jwts.builder()
	                .setSubject(String.valueOf(memberId))   // 내부 식별자
	                .setIssuedAt(now)
	                .setExpiration(new Date(now.getTime() + accessTtl))
	                .signWith(key, SignatureAlgorithm.HS256);

	        if (provider != null) b.claim("provider", provider);
	        if (roles != null && !roles.isEmpty()) b.claim("roles", roles);
	        return b.compact();
	    }

	    /** Refresh Token 발급 */
	    public String generateRefreshToken(Long memberId) {
	        Date now = new Date();
	        return Jwts.builder()
	                .setSubject(String.valueOf(memberId))
	                .claim("typ", "refresh")
	                .setIssuedAt(now)
	                .setExpiration(new Date(now.getTime() + refreshTtl))
	                .signWith(key, SignatureAlgorithm.HS256)
	                .compact();
	    }

	    /** 검증 + Claims 반환 (유효하지 않으면 JwtException 발생) */
	    public Claims parseClaims(String token) throws JwtException {
	        return Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    /** 토큰 유효성만 체크 */
	    public boolean isValid(String token) {
	        try { parseClaims(token); return true; }
	        catch (JwtException | IllegalArgumentException e) { return false; }
	    }

	    public Long getMemberId(String token) {
	        return Long.valueOf(parseClaims(token).getSubject());
	    }

	    @SuppressWarnings("unchecked")
	    public List<String> getRoles(String token) {
	        Object v = parseClaims(token).get("roles");
	        return (v instanceof List) ? (List<String>) v : List.of();
	    }
	    
	 // 리프레시 토큰 여부 확인 (typ이 "refresh"인지 체크)
	    public boolean isRefreshToken(String token) {
	        try {
	            Claims claims = parseClaims(token);
	            return "refresh".equals(claims.get("typ"));
	        } catch (Exception e) {
	            return false;
	        }
	    }

}
