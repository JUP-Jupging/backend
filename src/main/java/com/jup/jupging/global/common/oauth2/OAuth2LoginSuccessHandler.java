package com.jup.jupging.global.common.oauth2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("OAuth2 로그인 성공!");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2User로부터 email 정보를 추출
//        String email = (String) oAuth2User.getAttributes().get("email");
        Long memberId =  (Long) oAuth2User.getAttributes().get("memberId");
        
        // email이 없다면 예외 발생 (카카오 비동의 등)
        if (memberId == null) {
            log.error("로그인한 사용자의 이메일 정보를 찾을 수 없습니다.");
            response.sendRedirect("/login-error"); // 예시 에러 처리
            return;
        }

        // 1. Access Token 생성
        String accessToken = jwtUtil.createAccessToken(memberId);
        log.info("Access Token 생성 완료: {}", accessToken);

        // 2. Refresh Token 생성
        String refreshToken = jwtUtil.createRefreshToken(memberId);
        log.info("Refresh Token 생성 완료: {}", refreshToken);

        // 3. Redis에 Refresh Token 저장 (Key: email, Value: refreshToken)
        redisTemplate.opsForValue().set(
        		String.valueOf(memberId),
                refreshToken,
                7, // 만료 시간
                TimeUnit.DAYS // 만료 시간 단위
        );
        
        // 4. Refresh Token을 HttpOnly 쿠키에 담아 클라이언트에 전달
        addRefreshTokenToCookie(response, refreshToken);

        // 5. Access Token은 URL 파라미터로 전달
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth-redirect")
                .queryParam("token", accessToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        
        response.sendRedirect(targetUrl);
	}
	
	private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
        refreshTokenCookie.setSecure(true);   // HTTPS 연결 시에만 전송
        refreshTokenCookie.setPath("/");      // 전체 경로에서 접근 가능
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 유효 기간 (7일)
        response.addCookie(refreshTokenCookie);
    }
	
}
