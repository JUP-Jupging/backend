package com.jup.jupging.domain.trail.mapper;

import com.jup.jupging.domain.trail.dto.TrailDto;
import com.jup.jupging.domain.trail.entity.Trail;

public class TrailMapper {

    public static TrailDto toTrailDto(Trail trail) {
        if (trail == null) return null;

        return TrailDto.builder()
                .trailId(trail.getTrailId())
                .trailName(trail.getTrailName())
                .trailTypeName(trail.getTrailTypeName())
                .length(trail.getLength())
                .lengthDetail(trail.getLengthDetail())
                .difficultyLevel(trail.getDifficultyLevel())
                .trackTime(trail.getTrackTime())
                .cityName(trail.getCityName())
                .lotNumberAddress(trail.getLotNumberAddress())
                .spotLatitude(trail.getSpotLatitude())
                .spotLongitude(trail.getSpotLongitude())
                .amenityDescription(trail.getAmenityDescription())
                .toiletDescription(trail.getToiletDescription())
                .optionDescription(trail.getOptionDescription())
                .description(trail.getDescription())
                .descriptionDetail(trail.getDescriptionDetail())
                .reportCount(trail.getReportCount())
                .img1(trail.getImageUrl1())
                .img2(trail.getImageUrl2())
                .build();
    }

}
