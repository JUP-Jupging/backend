package com.jup.jupging.global.oauth.dto;


import lombok.Data;

@Data

public class GoogleUserInfo {
	
	private String email;
	private String nickname;
	private String provider = "google";
}
