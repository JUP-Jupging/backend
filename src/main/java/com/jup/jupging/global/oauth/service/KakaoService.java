package com.jup.jupging.global.oauth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jup.jupging.global.oauth.dto.KakaoTokenResponse;
import com.jup.jupging.global.oauth.dto.KakaoUserInfo;

@Service 
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String code) {
    	 System.out.println("🔑 code: " + code);
    	 System.out.println("📦 clientId: " + clientId);
    	 System.out.println("📦 redirectUri: " + redirectUri);
    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", request, KakaoTokenResponse.class);

        return response.getBody().getAccess_token();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        KakaoUserInfo userInfo = new KakaoUserInfo();
        userInfo.setProvider("kakao");
        userInfo.setEmail((String) kakaoAccount.get("email"));
        userInfo.setNickname((String) profile.get("nickname"));
        return userInfo;
    }
    
    public void kakaoLogout(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(
                "https://kapi.kakao.com/v1/user/logout",
                request,
                String.class
            );
        } catch (Exception e) {
            // 로그아웃 실패해도 무시할 수 있도록 로그만 남김
            System.err.println("카카오 로그아웃 실패: " + e.getMessage());
        }
    }
    
}
