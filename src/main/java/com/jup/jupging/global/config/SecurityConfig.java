package com.jup.jupging.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jup.jupging.domain.member.service.CustomOAuth2MemberService;
import com.jup.jupging.global.common.oauth2.CustomAuthenticationEntryPoint;
import com.jup.jupging.global.common.oauth2.JwtAuthenticationFilter;
import com.jup.jupging.global.common.oauth2.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
	
	private final CustomOAuth2MemberService customOAuth2MemberService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
        .csrf(csrf -> csrf.disable())
        // JWT를 사용할 것이므로 세션은 stateless하게 관리
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(formLogin -> formLogin.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        
        // 경로별 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/login", "/login/oauth2", "/oauth2/**", "/api/v1/token/reissue", "/login-error", "/oauth-redirect", "/error").permitAll()
                .anyRequest().permitAll()
//                .anyRequest().authenticated()
        )
        
        // OAuth2 로그인 설정
        .oauth2Login(oauth2 -> oauth2
        		.redirectionEndpoint(redirection -> redirection
        				.baseUri("http://localhost:8080/login/oauth2/code/kakao"))
        		.successHandler(oAuth2LoginSuccessHandler)
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2MemberService) // 로그인 성공 후 사용자 정보를 처리할 서비스 등록
                )
        )
        
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        
//        // Exception Handling 설정 추가
//        .exceptionHandling(exception -> exception
//                .authenticationEntryPoint(customAuthenticationEntryPoint)
//        )
        ;

        return http.build();
    }
}
