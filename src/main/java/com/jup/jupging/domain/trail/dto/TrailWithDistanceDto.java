package com.jup.jupging.domain.trail.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor // 모든 필드 받는 생성자
@NoArgsConstructor  // JPA Projection을 위해 기본 생성자도
public class TrailWithDistanceDto {

    private Long trailId;  // 숫자 자동 할당
    private String trailTypeName;         // 산책 경로 구분명
    private String trailName;             // 산책 경로명
    private String cityName;              // 시군구명
    private String difficultyLevel;       // 경로 난이도
    private String length;                // 경로 길이
    private double lengthDetail;          // 경로 상세 길이
    private String trackTime;             // 경로 소요 시간
    private String lotNumberAddress;      // 지번 주소
    private double spotLatitude;          // 위도
    private double spotLongitude;         // 경도
    private int reportCount;              // 제보 횟수
    private double distanceToUser;      // + 사용자 위치 간 거리, 조회

}
