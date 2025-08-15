package com.jup.jupging.domain.plogging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class PloggingRequestDto {
    private Long memberId;
    private Long trailId;
    private boolean isStart;
    private Double startLat;
    private Double startLng;
    private String ploggingTime;
    private Double distance;
    private List<Long> reportIds;
    private List<PloggingTrashDto> ploggingTrashDtoList;

}
