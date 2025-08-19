package com.jup.jupging.domain.plogging.dto;

import lombok.Data;

@Data
public class PloggingInsertRequestDto {
	private Long trailId;
	private String ploggingTime;
	private double distance;
	private Long memberId;
	private String imageUrl;
}
