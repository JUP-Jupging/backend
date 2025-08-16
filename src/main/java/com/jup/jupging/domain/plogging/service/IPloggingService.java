package com.jup.jupging.domain.plogging.service;

import java.util.List;

import com.jup.jupging.domain.plogging.dto.PloggingDto;

public interface IPloggingService {
	List<PloggingDto> findMyPlogging(Long memberId);
}
