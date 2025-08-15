package com.jup.jupging.domain.member.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;
import com.jup.jupging.global.common.oauth2.OAuthAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

	private final MemberMapper memberMapper;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("customoauth2memberservice 진입 성공");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info("중간 점검");
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("saveorupdate 메소드 실행 전");
        MemberDto member = saveOrUpdate(attributes);
        log.info("saveorupdate 메소드 실행 후");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
	}
	
	private MemberDto saveOrUpdate(OAuthAttributes attributes) {
		log.info("saveorupdate 실행");
		MemberDto member = memberMapper.findByEmail(attributes.getEmail())
                .map(entity -> {
                    entity.setNickname(attributes.getNickname());
//                    memberMapper.update(entity);
                    return entity;
                })
                .orElseGet(() -> {
                	MemberDto newMember = attributes.toEntity();
                    memberMapper.insert(newMember);
                    return newMember;
                });
		log.info("saveorupdate 리턴 직전");
        return member;
    }
	
}
