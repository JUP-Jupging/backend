package com.jup.jupging.domain.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	public List<MemberDto> getAllMembers() {
		return memberMapper.findAll();
	}

}
