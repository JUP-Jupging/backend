package com.jup.jupging.domain.plogging.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {

    private Long reportId;
    private String title;
    private Long trailId;
    private String imageUrl;
    private Integer paper;
    private Integer can;
    private Integer plastic;
    private Integer vinyl;
    private Integer glass;
    private Integer styro;
    private Integer battery;
    private LocalDate createdAt;

}