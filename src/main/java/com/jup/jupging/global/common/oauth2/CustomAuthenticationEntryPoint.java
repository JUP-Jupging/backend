package com.jup.jupging.global.common.oauth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("인증되지 않은 사용자의 접근: {}", authException.getMessage());

        // 응답 상태를 401 Unauthorized로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 응답 컨텐츠 타입을 JSON으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 본문 생성
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "인증이 필요합니다. 로그인 후 이용해주세요.");
        body.put("path", request.getRequestURI());

        // 응답 본문에 JSON 쓰기
        objectMapper.writeValue(response.getOutputStream(), body);
	}

}
