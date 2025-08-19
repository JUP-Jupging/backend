package com.jup.jupging.domain.plogging.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingInsertRequestDto;

@Mapper
public interface PloggingMapper {
	List<PloggingDto> findMyPlogging(Long memberId);
}
