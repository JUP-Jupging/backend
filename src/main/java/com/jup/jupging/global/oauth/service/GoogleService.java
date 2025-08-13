package com.jup.jupging.global.oauth.service;



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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jup.jupging.global.oauth.dto.GoogleUserInfo;

@Service
public class GoogleService {
	 @Value("${google.client-id}")
	    private String clientId;

	    @Value("${google.client-secret}")
	    private String clientSecret;

	    @Value("${google.redirect-uri}")
	    private String redirectUri;

	    private final RestTemplate restTemplate = new RestTemplate();

	    public String getAccessToken(String code) {
	        String url = "https://oauth2.googleapis.com/token";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("code", code);
	        params.add("client_id", clientId);
	        params.add("client_secret", clientSecret);
	        params.add("redirect_uri", redirectUri);
	        params.add("grant_type", "authorization_code");

	        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
	        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode json = objectMapper.readTree(response.getBody());
	            return json.get("access_token").asText();
	        } catch (Exception e) {
	            throw new RuntimeException("Google AccessToken 파싱 실패", e);
	        }
	    }

	    public GoogleUserInfo getUserInfo(String accessToken) {
	        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(accessToken);

	        HttpEntity<?> request = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode json = objectMapper.readTree(response.getBody());

	            String email = json.get("email").asText();
	            String name = json.get("name").asText();

	            GoogleUserInfo userInfo = new GoogleUserInfo();
	            userInfo.setEmail(email);
	            userInfo.setNickname(name);
	            userInfo.setProvider("google");

	            return userInfo;
	        } catch (Exception e) {
	            throw new RuntimeException("Google 사용자 정보 파싱 실패", e);
	        }
	    }
	    
}
