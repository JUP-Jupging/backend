package com.jup.jupging.global.oauth.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;
import com.jup.jupging.global.oauth.dto.GoogleUserInfo;
import com.jup.jupging.global.oauth.dto.KakaoUserInfo;
import com.jup.jupging.global.oauth.jwt.JwtUtil;

@Service
public class AuthService {
	 private final KakaoService kakaoService;
	    private final GoogleService googleService;
	    private final MemberMapper memberMapper;
	    private final JwtUtil jwtUtil;

	    public AuthService(KakaoService kakaoService, GoogleService googleService,
	                       MemberMapper memberMapper, JwtUtil jwtUtil) {
	        this.kakaoService = kakaoService;
	        this.googleService = googleService;
	        this.memberMapper = memberMapper;
	        this.jwtUtil = jwtUtil;
	    }

	    // 🔹 카카오 로그인 처리
	    public Map<String, Object> handleKakaoLogin(String code) {
	        String kakaoAccessToken = kakaoService.getAccessToken(code);
	        KakaoUserInfo kakaoUser = kakaoService.getUserInfo(kakaoAccessToken);

	        MemberDto member = findOrCreate(kakaoUser.getEmail(), kakaoUser.getNickname(), "kakao");

	        return issueJwtResponse(member);
	    }

	    // 🔹 구글 로그인 처리
	    public Map<String, Object> handleGoogleLogin(String code) {
	        String googleAccessToken = googleService.getAccessToken(code);
	        GoogleUserInfo googleUser = googleService.getUserInfo(googleAccessToken);

	        MemberDto member = findOrCreate(googleUser.getEmail(), googleUser.getNickname(), "google");

	        return issueJwtResponse(member);
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

	    // 🔹 JWT 발급 + 응답 데이터 구성
	    private Map<String, Object> issueJwtResponse(MemberDto member) {
	        String accessToken = jwtUtil.generateAccessToken(member.getMemberId(), member.getProvider(), List.of("USER"));
	        String refreshToken = jwtUtil.generateRefreshToken(member.getMemberId());

	        return Map.of(
	                "accessToken", accessToken,
	                "refreshToken", refreshToken,
	                "memberId", member.getMemberId(),
	                "provider", member.getProvider()
	        );
	    }
}
