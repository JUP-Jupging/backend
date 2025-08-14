package com.jup.jupging.global.common.oauth2;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {
	
	private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request) {
        // 1. 쿠키에서 Refresh Token 추출
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Refresh Token이 없는 경우
        if (!StringUtils.hasText(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing.");
        }

        // 3. Refresh Token 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
        }

        // 4. 토큰에서 이메일 추출 및 Redis와 대조
        String email = jwtUtil.getEmailFromToken(refreshToken);
        String redisRefreshToken = redisTemplate.opsForValue().get(email);

        if (!refreshToken.equals(redisRefreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token mismatch.");
        }

        // 5. 새로운 Access Token 생성
        String newAccessToken = jwtUtil.createAccessToken(email);

        return ResponseEntity.ok(newAccessToken);
    }
}
