package com.jup.jupging.global.oauth.controller;



import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;
import com.jup.jupging.global.oauth.jwt.JwtUtil;
import com.jup.jupging.global.oauth.service.AuthService;
import com.jup.jupging.global.oauth.service.GoogleService;
import com.jup.jupging.global.oauth.service.KakaoService;
@RestController
@RequestMapping("/oauth")
public class AuthController {
//	private final KakaoService kakaoService;
//    private final MemberMapper memberMapper;
//    private final GoogleService googleService;
//    public AuthController(KakaoService kakaoService, GoogleService googleService,MemberMapper memberMapper) {
//        this.kakaoService = kakaoService;
//        this.googleService = googleService;
//        this.memberMapper = memberMapper;
//    }
//    
//    //카카오 로그인 콜백
//    @GetMapping("/kakao/callback")
//    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
//        String accessToken = kakaoService.getAccessToken(code);
//        KakaoUserInfo kakaoUser = kakaoService.getUserInfo(accessToken);
//
//        MemberDto member = memberMapper.findByEmailIncludeDeleted(kakaoUser.getEmail());
//        if (member != null) {
//        	//소프트 삭제된 계정이면 복구
//        	if("Y".equals(member.getIsDeleted())) {
//        		memberMapper.reactivate(member.getId());
//        		member.setIsDeleted("N");
//        	}
//        }else {
//        	//신규 회원 등록
//            member = new MemberDto();
//            member.setEmail(kakaoUser.getEmail());
//            member.setNickname(kakaoUser.getNickname());
//            member.setProvider(kakaoUser.getProvider());
//            memberMapper.insert(member);
//        }
//
//        session.setAttribute("loginMember", member);
//        session.setAttribute("accessToken", accessToken);
//        return ResponseEntity.ok("로그인 성공! 세션 생성됨");
//    }
//    
//    // 구글 로그인 콜백
//    @GetMapping("/google/callback")
//    public ResponseEntity<String> googleCallback(@RequestParam("code") String code, HttpSession session) {
//        String accessToken = googleService.getAccessToken(code);
//        GoogleUserInfo googleUser = googleService.getUserInfo(accessToken);
//
//        MemberDto member = memberMapper.findByEmailIncludeDeleted(googleUser.getEmail());
//        if (member != null) {
//        	//소프트 삭제된 계정이면 복구
//        	if("Y".equals(member.getIsDeleted())) {
//        		memberMapper.reactivate(member.getId());
//        		member.setIsDeleted("N");
//        	}
//        }else {
//        	//신규 회원 등록
//            member = new MemberDto();
//            member.setEmail(googleUser.getEmail());
//            member.setNickname(googleUser.getNickname());
//            member.setProvider(googleUser.getProvider());
//            memberMapper.insert(member);
//        }
//
//        session.setAttribute("loginMember", member);
//        session.setAttribute("accessToken", accessToken);
//
//        return ResponseEntity.ok("구글 로그인 성공! 세션 생성됨");
//    }
	
	
	
	
    
//    //로그인한 사용자 정보 조회
//    @GetMapping("/me")
//    public ResponseEntity<?> getSessionUser(HttpSession session) {
//        MemberDto member = (MemberDto) session.getAttribute("loginMember");
//        return member != null ? ResponseEntity.ok(member) : ResponseEntity.status(401).body("로그인 안됨");
//    }
//    
//    //로그아웃
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpSession session) {
//        String accessToken = (String) session.getAttribute("accessToken");
//
//        // 1. 카카오 로그아웃
//        if (accessToken != null) {
//            kakaoService.kakaoLogout(accessToken);
//        }
//
//        // 2. 세션 로그아웃
//        session.invalidate();
//
//        return ResponseEntity.ok("카카오 로그아웃 및 세션 종료 완료");
//    }
//    
//    //회원 탈퇴
//    @PostMapping("/withdraw")
//    public ResponseEntity<String> withdraw(HttpSession session){
//    	MemberDto member = (MemberDto) session.getAttribute("loginMember");
//    	String accessToken = (String) session.getAttribute("accessToken");
//    	
//    	if(member == null) {
//    		return ResponseEntity.status(401).body("로그인 안됨");
//    	}
//    	
////    	// 소셜 연결 해제
////    	if("kakao".equalsIgnoreCase(member.getProvider())) {
////    		kakaoService.kakaoUnlink(accessToken);
////    	}else if("google".equalsIgnoreCase(member.getProvider()))){
////    		googleService.googleRevoke(accessToken);
////    	}
//    	
//    	//DB 소프트 삭제
//    	memberMapper.softDeleteById(member.getId());
//    	
//    	//세션 종료
//    	session.invalidate();
//    	
//    	return ResponseEntity.ok("회원 탈퇴 완료");
//    }
	
	
	private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final MemberMapper memberMapper;
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    public AuthController(AuthService authService, JwtUtil jwtUtil, MemberMapper memberMapper,
                          KakaoService kakaoService, GoogleService googleService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.memberMapper = memberMapper;
        this.kakaoService = kakaoService;
        this.googleService = googleService;
    }

    // ✅ 카카오 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.handleKakaoLogin(code));
    }

    // ✅ 구글 로그인
    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.handleGoogleLogin(code));
    }

    // ✅ 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        String token = resolveToken(authHeader);
        if (!jwtUtil.isValid(token)) {
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
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("프론트에서 토큰 삭제 시 로그아웃 완료");
    }

    // ✅ 회원 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader("Authorization") String authHeader) {
        String token = resolveToken(authHeader);
        if (!jwtUtil.isValid(token)) {
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
	
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = resolveToken(authHeader);

        // 1. refresh token 유효성 검증
        if (!jwtUtil.isValid(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("유효하지 않은 refreshToken");
        }

        // 2. 사용자 조회
        Long memberId = jwtUtil.getMemberId(refreshToken);
        MemberDto member = memberMapper.findById(memberId);

        if (member == null || "Y".equals(member.getIsDeleted())) {
            return ResponseEntity.status(404).body("회원 정보를 찾을 수 없음");
        }

        // 3. roles 임의 부여 (기본 USER)
        List<String> roles = List.of("USER");

        // 4. access token 재발급
        String newAccessToken = jwtUtil.generateAccessToken(
            member.getMemberId(),
            member.getProvider(),
            roles
        );

        return ResponseEntity.ok(Map.of(
            "accessToken", newAccessToken
        ));
    }
	
    
}
