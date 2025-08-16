package com.jup.jupging.domain.plogging.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class Plogging {
	private Long ploggingId;
	private Long trailId;
	private Long memberId;
	private Date ploggingDate;
	private String ploggingTime;
	private Double distance;
	private Double startLat;
	private Double startLNG;
	private String ImageUrl;
	
}	
