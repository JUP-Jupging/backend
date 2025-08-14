package com.jup.jupging.domain.trashcan.mapper;

import com.jup.jupging.domain.trashcan.dto.TrashCanDto;
import com.jup.jupging.domain.trashcan.entity.TrashCan;

public class TrashCanMapper {

    //dto -> entity
    public static TrashCan toEntity(TrashCanDto dto) {
        TrashCan entity = new TrashCan();
        entity.setTrashCanId(dto.getTrashCanId());
        entity.setPlaceName(dto.getPlaceName());
        entity.setCityName(dto.getCityName());
        entity.setDistrictName(dto.getDistrictName());
        entity.setRoadAddress(dto.getRoadAddress());
        entity.setLotNumberAddress(dto.getLotNumberAddress());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setTrashCanType(dto.getTrashCanType());
        entity.setInstitution(dto.getInstitution());
        entity.setInstitutionTel(dto.getInstitutionTel());
        return entity;
    }

    // entity -> dto
    public static TrashCanDto toDto(TrashCan entity) {
        TrashCanDto dto = new TrashCanDto();
        dto.setTrashCanId(entity.getTrashCanId());
        dto.setPlaceName(entity.getPlaceName());
        dto.setCityName(entity.getCityName());
        dto.setDistrictName(entity.getDistrictName());
        dto.setRoadAddress(entity.getRoadAddress());
        dto.setLotNumberAddress(entity.getLotNumberAddress());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setTrashCanType(entity.getTrashCanType());
        dto.setInstitution(entity.getInstitution());
        dto.setInstitutionTel(entity.getInstitutionTel());
        return dto;
    }
}
