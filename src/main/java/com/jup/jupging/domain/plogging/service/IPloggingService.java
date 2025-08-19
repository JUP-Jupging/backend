package com.jup.jupging.domain.plogging.service;

import java.util.List;

import com.jup.jupging.domain.plogging.dto.MyPloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingInsertRequestDto;

public interface IPloggingService {
	List<MyPloggingDto> findMyPlogging(Long memberId);

	void insertPlogging(PloggingInsertRequestDto ploggingInsertRequestDto);
}
