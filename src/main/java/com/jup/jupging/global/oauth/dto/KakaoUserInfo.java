package com.jup.jupging.global.oauth.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
	
    private String email;
    private String nickname;
    private String provider = "kakao";
}
