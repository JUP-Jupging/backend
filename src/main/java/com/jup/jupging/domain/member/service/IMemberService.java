package com.jup.jupging.domain.member.service;

import java.util.List;

import com.jup.jupging.domain.member.dto.MemberDto;

public interface IMemberService {
	List<MemberDto> getAllMembers();
    MemberDto getById(Long memberId);
    void updateAppNickname(Long memberId, String appNickname);
    void updateActivityRegion(Long memberId, String activityRegion);
    void updateProfileImageKey(Long memberId, String key);
    void softDelete(Long memberId);
    void reactivate(Long memberId);
}
