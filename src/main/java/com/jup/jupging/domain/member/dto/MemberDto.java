package com.jup.jupging.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private String userid;
	private String name;
	private String password;
	private String email;
	private String address;
}