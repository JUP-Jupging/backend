package com.jup.jupging.domain.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jup.jupging.domain.member.dto.MemberDto;
import com.jup.jupging.domain.member.mapper.MemberMapper;

@Service
public class MemberService implements IMemberService{
	
	@Autowired
	private MemberMapper memberMapper;
	
	 @Override
	    public List<MemberDto> getAllMembers() {
	        return memberMapper.findAll();
	    }

	    @Override
	    public MemberDto getById(Long memberId) {
	        return memberMapper.findById(memberId);
	    }

	    @Override
	    @Transactional
	    public void updateAppNickname(Long memberId, String appNickname) {
	        memberMapper.updateAppNickname(memberId, appNickname);
	    }

	    @Override
	    @Transactional
	    public void updateActivityRegion(Long memberId, String activityRegion) {
	        memberMapper.updateActivityRegion(memberId, activityRegion);
	    }

	    @Override
	    @Transactional
	    public void updateProfileImageKey(Long memberId, String key) {
	        memberMapper.updateProfileImageKey(memberId, key);
	    }

	    @Override
	    @Transactional
	    public void softDelete(Long memberId) {
	        memberMapper.softDeleteById(memberId);
	    }

	    @Override
	    @Transactional
	    public void reactivate(Long memberId) {
	        memberMapper.reactivate(memberId);
	    }

}
