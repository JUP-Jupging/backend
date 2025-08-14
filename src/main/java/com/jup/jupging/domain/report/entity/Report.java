package com.jup.jupging.domain.report.entity;

import lombok.Data;

@Data
public class Report {
	  private Long reportId;
	  private Long memberId;
	  private Long trailId;
	  private String title;
	  private Double lat;
	  private Double lng;
	  private String imageUrl;

	  private Integer paper;
	  private Integer can;
	  private Integer glass;
	  private Integer plastic;
	  private Integer vinyl;
	  private Integer styro;
	  private Integer battery;
	  private String isPicked;
}
