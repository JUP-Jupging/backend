package com.jup.jupging.domain.plogging.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jup.jupging.domain.plogging.dto.MyPloggingDto;

@Mapper
public interface PloggingMapper {
	List<MyPloggingDto> findMyPlogging(Long memberId);
}
