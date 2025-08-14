package com.jup.jupging.domain.report.dto;

import lombok.Data;

@Data
public class ReportSummaryDto {
	
	private Long reportId;
	private String title;
	private Double lat; 
	private Double lng;
	private String imageUrl;
	private Long trailId;
	private String trailName;
	private String trailTypeName;
	private String isPicked;
	
}
