package com.jup.jupging.global.common.oauth2;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	private final Key key;
    private final long accessTokenExpirationMillis;
    private final long refreshTokenExpirationMillis;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration-millis}") long accessTokenExpirationMillis,
            @Value("${jwt.refresh-token-expiration-millis}") long refreshTokenExpirationMillis
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
    }
    
    public long getRefreshTokenExpirationMillis() {
		 return refreshTokenExpirationMillis;
	 }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Long memberId) {
    	return createToken(memberId, accessTokenExpirationMillis);
    }
    
    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshTokenExpirationMillis);
    }
    
    /**
     * 토큰 생성 로직 통합
     */
    private String createToken(Long memberId, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 토큰에서 사용자 이메일 추출
     * @param token 검증할 Access Token
     * @return 사용자 이메일
     */
    public String getMemberIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰의 유효성 검증
     * @param token 검증할 Access Token
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
        	log.info("validateToken : "+token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
    
    /** 검증 + Claims 반환 (유효하지 않으면 JwtException 발생) */
	 public Claims parseClaims(String token) throws JwtException {
		 return Jwts.parserBuilder()
				 .setSigningKey(key)
				 .build()
				 .parseClaimsJws(token)
				 .getBody();
	 }
	 
	 public Long getMemberId(String token) {
		 return Long.valueOf(parseClaims(token).getSubject());
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
