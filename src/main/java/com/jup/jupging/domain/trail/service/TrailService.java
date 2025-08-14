package com.jup.jupging.domain.trail.service;

import com.opencsv.CSVReader;
import com.jup.jupging.domain.trail.dto.TrailDto;
import com.jup.jupging.domain.trail.dto.TrailWithDistanceDto;
import com.jup.jupging.domain.trail.entity.Trail;
import com.jup.jupging.domain.trail.mapper.TrailMapper;
import com.jup.jupging.domain.trail.repository.TrailRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrailService {

    private static final String FILE_PATH = "src/main/resources/data/walk_street_info_open_data.csv";
    private final TrailRepository trailRepository;

    public TrailService(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }


    // 문자열, NUll 값 걸러내고 숫자 변환 메서드 1
    public boolean isNumber(String str){
        if(str == null || str.isBlank()){ // null, 빈 문자열
            return false;
        }
        try{
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // 문자열, NUll 값 걸러내고 숫자 변환 메서드 2
    public static boolean isDecimal(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }




    @Transactional
    public String insertAllTrailData() {
        List<Trail> trailsData = new ArrayList<>();

        try (   Reader reader = new FileReader(FILE_PATH);
                CSVReader csvReader = new CSVReader(reader)) {
                String[] line;

            // 엔티티로 변환할 때
            while ((line = csvReader.readNext()) != null) {
                // 숫자 검사
                if(!isDecimal(line[6])) continue;
                // CSV 컬럼 순서에 맞게 매핑
                trailsData.add(new Trail(
                        line[0],                  // trailTypeName
                        line[1],                  // trailName
                        line[2],                  // description
                        line[3],                  // cityName
                        line[4],                  // difficultyLevel
                        line[5],                  // length
                        Double.parseDouble(line[6]),   // lengthDetail
                        line[7],                  // descriptionDetail
                        line[8],                  // trackTime
                        line[9],                  // optionDescription
                        line[10],                 // toiletDescription
                        line[11],                 // amenityDescription
                        line[12],                 // lotNumberAddress
                        Double.parseDouble(line[13]),  // spotLatitude
                        Double.parseDouble(line[14]),   // spotLongtitude
                        0
                ));
            }
            trailRepository.saveAll(trailsData);
            trailRepository.flush(); // insert 실행

            // 10개만 미리보기
//            trailRepository.saveAll(trailsData.stream().limit(10).collect(Collectors.toList()));
            return "산책로 데이터 insert 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
    }


    // 특정 산책로 상세 조회
    @Transactional
    public TrailDto getTrailDetail(Long trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책로가 존재하지 않습니다. ID: " + trailId));

        return TrailMapper.toTrailDto(trail);
    }


    // 산책로 검색
    public List<TrailDto> findTrailsByKeyword(String keyword) {
        List<Trail> findTrails = trailRepository.findAllByTrailTypeNameOrTrailNameOrCityName(keyword);
        List<TrailDto> trailDtos = findTrails.stream().map(TrailMapper::toTrailDto).toList();

        // 검색 결과가 없으면 빈 List 반환
        System.out.println("trailDtos count :  " + trailDtos.size());
        return trailDtos;
    }


    // 산책로 조회 필터링
    public List<TrailDto> findTrails(String difficulty, String cityName) {
        Specification<Trail> spec = (root, query, cb) -> cb.conjunction();

        if (difficulty != null && !difficulty.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("difficultyLevel"), difficulty));
        }
        if (cityName != null && !cityName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("cityName"), cityName));
        }
        List<Trail> trails = trailRepository.findAll(spec);
        System.out.println("필터링 산책로 개수 : " + trails.size()); // 필터링 조회 확인

        List<TrailDto> trailDtos = trails.stream().map(TrailMapper::toTrailDto).toList();
        return trailDtos;
    }



    // 산책로 거리순 정렬할 때마다 요청
    public List<TrailWithDistanceDto> findTrailsNearby(double userLat, double userLong) {

        List<Object[]> results = trailRepository.findTrailsNearby(userLat, userLong);
        return results.stream()
                .map(r -> {
                    TrailWithDistanceDto dto = new TrailWithDistanceDto();
                    dto.setTrailId(((Number) r[0]).longValue());
                    dto.setTrailTypeName((String) r[1]);
                    dto.setTrailName((String) r[2]);
                    dto.setCityName((String) r[3]);
                    dto.setDifficultyLevel((String) r[4]);
                    dto.setLength((String) r[5]);
                    dto.setLengthDetail(((Number) r[6]).doubleValue());
                    dto.setTrackTime((String) r[7]);
                    dto.setLotNumberAddress((String) r[8]);
                    dto.setSpotLatitude(((Number) r[9]).doubleValue());
                    dto.setSpotLongitude(((Number) r[10]).doubleValue());
                    dto.setReportCount(((Number) r[11]).intValue());
                    dto.setDistanceToUser(((Number) r[12]).doubleValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public TrailWithDistanceDto findNearestTrail(double userLat, double userLong) {

        List<Object[]> results = trailRepository.findNearestTrail(userLat, userLong);

        if(!results.isEmpty()){
            TrailWithDistanceDto nearest = results.stream()
                    .map(r -> {
                        TrailWithDistanceDto dto = new TrailWithDistanceDto();
                        dto.setTrailId(((Number) r[0]).longValue());
                        dto.setTrailTypeName((String) r[1]);
                        dto.setTrailName((String) r[2]);
                        dto.setCityName((String) r[3]);
                        dto.setDifficultyLevel((String) r[4]);
                        dto.setLength((String) r[5]);
                        dto.setLengthDetail(((Number) r[6]).doubleValue());
                        dto.setTrackTime((String) r[7]);
                        dto.setLotNumberAddress((String) r[8]);
                        dto.setSpotLatitude(((Number) r[9]).doubleValue());
                        dto.setSpotLongitude(((Number) r[10]).doubleValue());
                        dto.setReportCount(((Number) r[11]).intValue());
                        dto.setDistanceToUser(((Number) r[12]).doubleValue());
                        return dto;
                    })
                    .toList().get(0);

            return nearest;
        }else{
            throw new RuntimeException("근처 산책로가 없습니다.");
        }

    }
}
