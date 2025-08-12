package com.jup.jupging.domain.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.service.IMemberService;
import com.jup.jupging.global.oauth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	
	 private final IMemberService memberService;
	    private final JwtUtil jwtUtil;

	    private Long memberIdFrom(String authHeader) {
	        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
	        if (!jwtUtil.isValid(token)) throw new IllegalArgumentException("invalid token");
	        return jwtUtil.getMemberId(token);
	    }

	    // ✅ 내 정보 조회
	    @GetMapping("/me")
	    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
	        Long memberId = memberIdFrom(authHeader);
	        MemberDto me = memberService.getById(memberId);
	        if (me == null) return ResponseEntity.status(404).body("회원 정보를 찾을 수 없음");
	        return ResponseEntity.ok(me);
	    }

	    // ✅ 회원 탈퇴(소프트 삭제)
	    @DeleteMapping("/me")
	    public ResponseEntity<?> withdraw(@RequestHeader("Authorization") String authHeader) {
	        Long memberId = memberIdFrom(authHeader);
	        memberService.softDelete(memberId);
	        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 완료"));
	    }

	    // ✅ 앱 닉네임 수정
	    @PatchMapping("/me/app_nickname")
	    public ResponseEntity<?> changeAppNickname(@RequestHeader("Authorization") String authHeader,
	                                               @RequestBody AppNicknameReq req) {
	        Long memberId = memberIdFrom(authHeader);
	        memberService.updateAppNickname(memberId, req.appNickname());
	        return ResponseEntity.ok(Map.of("appNickname", req.appNickname()));
	    }

	    // ✅ 활동 지역 수정
	    @PatchMapping("/me/activity_region")
	    public ResponseEntity<?> changeActivityRegion(@RequestHeader("Authorization") String authHeader,
	                                                  @RequestBody ActivityRegionReq req) {
	        Long memberId = memberIdFrom(authHeader);
	        memberService.updateActivityRegion(memberId, req.activityRegion());
	        return ResponseEntity.ok(Map.of("activityRegion", req.activityRegion()));
	    }

	    // ✅ 프로필 이미지 반영 (S3 업로드 후 key 저장)
	    @PatchMapping("/me/profile_image")
	    public ResponseEntity<?> changeProfileImage(@RequestHeader("Authorization") String authHeader,
	                                                @RequestBody ProfileImageReq req) {
	        Long memberId = memberIdFrom(authHeader);
	        memberService.updateProfileImageKey(memberId, req.profileImageKey());
	        return ResponseEntity.ok(Map.of("profileImageKey", req.profileImageKey()));
	    }

	    // (선택) presigned URL 발급 API가 필요하면 POST /members/me/profile-image/upload-url 추가
	    // 프론트가 그 URL로 업로드 후 위 PATCH를 호출하는 흐름.
	    
	    // --- 요청 DTO ---
	    public record AppNicknameReq(String appNickname) {}
	    public record ActivityRegionReq(String activityRegion) {}
	    public record ProfileImageReq(String profileImageKey) {}

}
