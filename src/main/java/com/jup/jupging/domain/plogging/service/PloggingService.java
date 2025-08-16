package com.jup.jupging.domain.plogging.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.mapper.PloggingMapper;

@Service
public class PloggingService implements IPloggingService{

	@Autowired
	PloggingMapper ploggingMapper;
	
	@Override
	public List<PloggingDto> findMyPlogging(Long memberId) {
		return ploggingMapper.findMyPlogging(memberId);
	}

}
