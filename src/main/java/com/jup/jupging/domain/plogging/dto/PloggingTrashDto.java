package com.jup.jupging.domain.plogging.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jup.jupging.domain.plogging.entity.Plogging;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PloggingTrashDto {

    private Long reportId;
    private Double lat;
    private Double lng;
    private String imageUrl;
    private Integer paper;
    private Integer can;
    private Integer plastic;
    private Integer vinyl;
    private Integer glass;
    private Integer styro;
    private Integer battery;
    private LocalDate createdAt;
    private String title;
    private String isPicked;
}
