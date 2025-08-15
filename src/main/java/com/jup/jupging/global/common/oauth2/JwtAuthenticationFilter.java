package com.jup.jupging.global.common.oauth2;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jup.jupging.domain.member.mapper.MemberMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
    private final MemberMapper memberMapper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 1. 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사
        if (token != null && jwtUtil.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가져와서 SecurityContext에 저장
            String email = jwtUtil.getMemberIdFromToken(token);
            
            memberMapper.findByEmail(email).ifPresent(member -> {
            	String role = member.getRole();
            	if (!StringUtils.hasText(role)) { // null 또는 빈 문자열일 경우
            	    role = "ROLE_USER";
            	}
                // 인증 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        member, // principal (인증된 사용자 객체)
                        null, // credentials (자격 증명, 보통 null)
                        Collections.singleton(new SimpleGrantedAuthority(role)) // authorities (권한)
                );
                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), request.getRequestURI());
            });
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
	}
	
	/**
     * Request Header에서 토큰 정보를 꺼내오기
     * @param request HttpServletRequest 객체
     * @return 추출된 토큰 문자열
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
