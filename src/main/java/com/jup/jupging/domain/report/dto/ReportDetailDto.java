package com.jup.jupging.domain.report.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportDetailDto {
	private Long reportId;
	private Long memberId;
	private Long trailId;
	private String trailName;
	private String trailTypeName;
	private String title;
	private Double lat;
	private Double lng;
	private String imageUrl;
	private String isPicked;
	private Integer paper, can, plastic, vinyl, glass, styro, battery;
	private LocalDateTime createdAt;
}
