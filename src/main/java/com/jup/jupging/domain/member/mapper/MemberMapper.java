package com.jup.jupging.domain.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jup.jupging.domain.member.dto.MemberDto;

@Mapper
public interface MemberMapper {
	
	List<MemberDto> findAll();

}
