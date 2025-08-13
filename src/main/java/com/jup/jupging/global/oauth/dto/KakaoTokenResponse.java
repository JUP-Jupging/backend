package com.jup.jupging.global.oauth.dto;

import lombok.Data;

@Data
public class KakaoTokenResponse {
	private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
}
