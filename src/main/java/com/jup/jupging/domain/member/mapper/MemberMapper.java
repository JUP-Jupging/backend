package com.jup.jupging.domain.member.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jup.jupging.domain.member.dto.MemberDto;

@Mapper
public interface MemberMapper {
	
	List<MemberDto> findAll();
	Optional<MemberDto> findByEmail(String email);
	void insert(MemberDto memberdto);
	void softDeleteById(Long memberId);
	MemberDto findByEmailIncludeDeleted(String email);
	void reactivate(long memberId);
	MemberDto findById(Long memberId);
	
	 int updateAppNickname(@Param("memberId") Long memberId,
             @Param("appNickname") String appNickname);

	 int updateActivityRegion(@Param("memberId") Long memberId,
                @Param("activityRegion") String activityRegion);

	 int updateProfileImageKey(@Param("memberId") Long memberId,
                 @Param("profileImageKey") String profileImageKey);
}
