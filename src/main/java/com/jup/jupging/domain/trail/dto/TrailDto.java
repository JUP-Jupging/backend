package com.jup.jupging.domain.trail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrailDto {

    private Long trailId;  // 숫자 자동 할당
    private String trailTypeName;         // 산책 경로 구분명
    private String trailName;             // 산책 경로명
    private String description;           // 설명
    private String cityName;              // 시군구명
    private String difficultyLevel;                 // 경로 난이도
    private String length;                // 경로 길이
    private double lengthDetail;          // 경로 상세 길이
    private String descriptionDetail;     // 추가 설명
    private String trackTime;             // 경로 소요 시간
    private String optionDescription;     // 옵션 설명
    private String toiletDescription;     // 화장실 설명
    private String amenityDescription;    // 편의시설 설명
    private String lotNumberAddress;      // 지번 주소

    private double spotLatitude;          // 위도
    private double spotLongitude;         // 경도
    private int reportCount;              // 제보 횟수


    public TrailDto(Long trailId, String trailTypeName, String trailName, String description, String cityName, String difficultyLevel, String length, double lengthDetail, String descriptionDetail, String trackTime, String optionDescription,  String toiletDescription, String amenityDescription, String lotNumberAddress, double spotLatitude, double spotLongitude, int reportCount) {
        this.trailId = trailId;
        this.trailTypeName = trailTypeName;
        this.trailName = trailName;
        this.description = description;
        this.cityName = cityName;
        this.difficultyLevel = difficultyLevel;
        this.length = length;
        this.lengthDetail = lengthDetail;
        this.descriptionDetail = descriptionDetail;
        this.optionDescription = optionDescription;
        this.trackTime = trackTime;
        this.toiletDescription = toiletDescription;
        this.amenityDescription = amenityDescription;
        this.lotNumberAddress = lotNumberAddress;
        this.spotLatitude = spotLatitude;
        this.spotLongitude = spotLongitude;
        this.reportCount = reportCount;
    }

}
