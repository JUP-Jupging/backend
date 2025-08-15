package com.jup.jupging.global.common.oauth2;

import java.util.Map;

import com.jup.jupging.domain.member.dto.MemberDto;

import lombok.Data;

@Data
public class OAuthAttributes {
	private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String provider;
    private String providerId;
    
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    	OAuthAttributes attr = new OAuthAttributes();
    	attr.setNickname((String) attributes.get("name"));
    	attr.setEmail((String) attributes.get("email"));
    	attr.setProvider("google");
    	attr.setProviderId((String) attributes.get("sub"));
    	attr.setAttributes(attributes);
    	attr.setNameAttributeKey(userNameAttributeName);
    	return attr;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        
        OAuthAttributes attr = new OAuthAttributes();
    	attr.setNickname((String) kakaoProfile.get("nickname"));
    	attr.setEmail((String) kakaoAccount.get("email"));
    	attr.setProvider("kakao");
    	attr.setProviderId(String.valueOf(attributes.get("id")));
    	attr.setAttributes(attributes);
    	attr.setNameAttributeKey(userNameAttributeName);
    	return attr;
    }

    public MemberDto toEntity() {
    	MemberDto member = new MemberDto();
    	member.setNickname(nickname);
    	member.setEmail(email);
    	member.setRole("ROLE_USER");
    	member.setProvider(provider);
    	member.setProviderId(providerId);
    	return member;
    }
}
