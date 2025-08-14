package com.jup.jupging.domain.report.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReportReq {

	private String title;
	private Double lat; 
	private Double lng;
	private Long trailId;
	private String imageUrl;
	private String isPicked;
	private Map<String,Integer> categoryCounts;
	
}
