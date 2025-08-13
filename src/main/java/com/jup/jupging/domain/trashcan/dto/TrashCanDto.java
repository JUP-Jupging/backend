package com.jup.jupging.domain.trashcan.dto;

import lombok.Data;

@Data
public class TrashCanDto {

    private Long trashCanId;
    private String placeName;
    private String cityName;
    private String districtName;
    private String roadAddress;
    private String lotNumberAddress;
    private double latitude;
    private double longitude;
    private String trashCanType;
    private String institution;
    private String institutionTel;
}