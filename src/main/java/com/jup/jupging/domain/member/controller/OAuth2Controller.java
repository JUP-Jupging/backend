package com.jup.jupging.domain.member.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;
import com.jup.jupging.global.common.oauth2.JwtUtil;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
	
	private final MemberMapper memberMapper;
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtUtil jwtUtil;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

	private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * 프론트엔드에게 소셜 로그인 URL을 반환하는 API
     * @param provider 소셜 로그인 종류 (kakao, google)
     * @return provider에 맞는 소셜 로그인 URL
     */
//    @GetMapping("/api/v1/auth/{provider}")
//    public Map<String, String> getSocialLoginUrl(@PathVariable("provider") String provider) {
//        // 1. application.yml에 등록된 OAuth2 클라이언트 정보를 가져온다.
//        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider.toLowerCase());
//
//        // 2. 해당 클라이언트의 인증 URL을 가져온다.
//        String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
//        String clientId = clientRegistration.getClientId();
//        String redirectUri = clientRegistration.getRedirectUri();
////        String scope = String.join(",", clientRegistration.getScopes());
//
//        // 3. 동적으로 URL을 생성한다. (state, response_type 등은 프레임워크가 자동으로 처리하는 부분을 수동으로 구성)
//        String socialLoginUrl = authorizationUri +
//                "?client_id=" + clientId +
//                "&redirect_uri=" + redirectUri +
//                "&response_type=code&prompt=login";
//
//        Map<String, String> urlMap = new HashMap<>();
//        urlMap.put("url", socialLoginUrl);
//        
//        return urlMap;
//    }
    
    @PostMapping("/auth/kakao/native")
//    public ResponseEntity<?> login(@RequestParam("code") String code) {
    public ResponseEntity<?> login(String accessToken) {
    	
//    	HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", clientId);
//        params.add("redirect_uri", redirectUri);
//        params.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
//                "https://kauth.kakao.com/oauth/token", request, KakaoTokenResponse.class);

//    	String kakaoAccessToken = response.getBody().getAccess_token();
    	String kakaoAccessToken = accessToken;
    	
    	HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(kakaoAccessToken);

        HttpEntity<Void> request2 = new HttpEntity<>(headers2);

        ResponseEntity<Map> response2 = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request2,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) response2.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        KakaoUserInfo userInfo = new KakaoUserInfo();
        userInfo.setProvider("kakao");
        userInfo.setEmail((String) kakaoAccount.get("email"));
        userInfo.setNickname((String) profile.get("nickname"));
    	
		KakaoUserInfo kakaoUser = userInfo;

		MemberDto member = findOrCreate(kakaoUser.getEmail(), kakaoUser.getNickname(), "kakao");
		Map<String, Object> issueJwtResponse = issueJwtResponse(member);
		
    	return ResponseEntity.ok(issueJwtResponse);
    }
    
    // 🔹 회원 조회 or 신규 등록 or 탈퇴 복구
 	private MemberDto findOrCreate(String email, String nickname, String provider) {
 		MemberDto member = memberMapper.findByEmailIncludeDeleted(email);
 		if (member != null && "Y".equals(member.getIsDeleted())) {
 			memberMapper.reactivate(member.getMemberId());
 			member.setIsDeleted("N");
 		} else if (member == null) {
 			member = new MemberDto();
 			member.setEmail(email);
 			member.setNickname(nickname);
 			member.setProvider(provider);
 			memberMapper.insert(member);
 		}
 		return member;
 	}
    
 	// 🔹 JWT 발급 + 응답 데이터 구성 + Redis에 토큰 저장
 	private Map<String, Object> issueJwtResponse(MemberDto member) {
 		String accessToken = jwtUtil.createAccessToken(member.getMemberId());
 		String refreshToken = jwtUtil.createRefreshToken(member.getMemberId());

 		// Redis에 Refresh Token 저장
 		redisTemplate.opsForValue().set(
 				"RT:" + member.getMemberId(), 
 				refreshToken,
 				jwtUtil.getRefreshTokenExpirationMillis(),
 				TimeUnit.MILLISECONDS
 				);

 		return Map.of(
 				"accessToken", accessToken,
 				"refreshToken", refreshToken,
 				"memberId", member.getMemberId(),
 				"provider", member.getProvider()
 				);
 	}
 	
 	@Data
 	public static class KakaoTokenResponse {
 		private String access_token;
 	    private String refresh_token;
 	    private String token_type;
 	    private Integer expires_in;
 	}
 	
 	@Data
 	public static class KakaoUserInfo {
 		
 	    private String email;
 	    private String nickname;
 	    private String provider = "kakao";
 	}
 	
 	// ✅ 내 정보 조회
    @GetMapping("/oauth/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        String token = resolveToken(authHeader);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰");
        }

        Long memberId = jwtUtil.getMemberId(token);
        MemberDto member = memberMapper.findById(memberId);
        if (member == null) {
            return ResponseEntity.status(404).body("회원 정보를 찾을 수 없음");
        }

        return ResponseEntity.ok(member);
    }
    
 // ✅ 로그아웃 (프론트에서 토큰 삭제하면 됨)
    @PostMapping("/oauth/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessTokenWithBearer) {
    	// 1. "Bearer " 접두어 제거
        String accessToken = accessTokenWithBearer.substring(7);

        // 2. Access Token에서 사용자 이메일 정보 가져오기
        Long memberId = Long.parseLong(jwtUtil.getMemberIdFromToken(accessToken));

        // 3. Redis에서 해당 유저의 Refresh Token 삭제 ⭐️
        if (redisTemplate.opsForValue().get("RT:" + memberId) != null) {
            redisTemplate.delete("RT:" + memberId);
        }
        return ResponseEntity.ok("프론트에서 토큰 삭제 시 로그아웃 완료");
    }

    // ✅ 회원 탈퇴
    @PostMapping("/oauth/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader("Authorization") String authHeader) {
        String token = resolveToken(authHeader);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰");
        }

        Long memberId = jwtUtil.getMemberId(token);
        memberMapper.softDeleteById(memberId);

        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // ✅ Bearer 토큰 추출 유틸
    private String resolveToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
	
    @PostMapping("/oauth/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = resolveToken(authHeader);

        // 1. refresh token 유효성 검증
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("유효하지 않은 refreshToken");
        }

        // 2. 사용자 조회
        Long memberId = jwtUtil.getMemberId(refreshToken);
        MemberDto member = memberMapper.findById(memberId);
        
        String savedRefreshToken = redisTemplate.opsForValue().get("RT:" + memberId);
        
     // 4. Redis에 토큰이 없거나, 요청된 토큰과 일치하지 않으면 예외 처리
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("저장된 토큰과 일치하지 않습니다.");
        }

        // 5. access token 재발급
        String newAccessToken = jwtUtil.createAccessToken(memberId);

        return ResponseEntity.ok(Map.of(
            "accessToken", newAccessToken
        ));
    }
    
}
