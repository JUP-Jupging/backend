package com.jup.jupging.domain.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping
	public List<MemberDto> getAll() {
		return memberService.getAllMembers();
	}

}
