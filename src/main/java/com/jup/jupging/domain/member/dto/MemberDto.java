package com.jup.jupging.domain.member.dto;



import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	 	private Long memberId;
	    private String email;        // 소셜 이메일
	    private String nickname;     // 소셜 닉네임(원본)
	    private String appNickname;  // 앱에서 설정한 닉네임
	    private String provider;     // kakao, google …
	    private String isDeleted;    // "Y" / "N"
	    private String profileImageUrl; // CDN 완성 URL(또는 key를 따로 들고가면 profileImageKey)
	    private String activityRegion;  // 활동 지역
	    private LocalDateTime createdAt; 
	    private String role;
	    private String providerId;
}