package com.jup.jupging.domain.plogging.dto;

import com.jup.jupging.domain.plogging.entity.Plogging;
import lombok.Data;
import java.sql.Date;


import java.time.LocalDate;
import java.util.List;

// 플로깅 등록
@Data
public class PloggingDto {

	private Long ploggingId;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private LocalDate ploggingDate;
    private Date ploggingDate2;
    private String ploggingTime;
    private Double distance;
    private String difficulty;
    private List<PloggingTrashDto> pickedTrashList;

    private Long memberId;
    private String trailName;
    private String cityName;
    private String image;

   private String trailId;
   private String trailTypeName;


    public PloggingDto(Plogging entity){
        this.startLat = entity.getStartLat();
        this.startLng = entity.getStartLng();
        this.ploggingTime = entity.getPloggingTime();
        this.ploggingDate = entity.getPloggingDate();
        this.distance = entity.getDistance();
        this.difficulty = entity.getTrail().getDifficultyLevel();
        this.trailName = entity.getTrail().getTrailName();
        this.cityName = entity.getTrail().getCityName();
        this.memberId = entity.getMember().getMemberId();
        this.image = entity.getImageUrl();
        this.pickedTrashList = entity.getTrashList().stream().map(e -> {
            PloggingTrashDto dto = new PloggingTrashDto();
            dto.setReportId(e.getReportId());
            dto.setLat(e.getLat());
            dto.setLng(e.getLng());
            dto.setImageUrl(e.getImageUrl());
            dto.setPaper(e.getPaper());
            dto.setCan(e.getCan());
            dto.setPlastic(e.getPlastic());
            dto.setVinyl(e.getVinyl());
            dto.setGlass(e.getGlass());
            dto.setStyro(e.getStyro());
            dto.setBattery(e.getBattery());
            dto.setCreatedAt(e.getCreatedAt());
            dto.setTitle(e.getTitle());
            dto.setIsPicked(e.getIsPicked());
            dto.setImageUrl(e.getImageUrl());
            return dto;
        }).toList();
    }
}
