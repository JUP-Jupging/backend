package com.jup.jupging.domain.trail.controller;


import com.jup.jupging.domain.trail.dto.*;
import com.jup.jupging.domain.trail.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/zubging/trails", produces = "application/json; charset=utf8")
public class TrailController {

    private final TrailService trailService;

    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    // DB INSERT용 메서드
    @PostMapping
    public ResponseEntity<?> insertAllTrailData() {
        String result = trailService.insertAllTrailData();
        return ResponseEntity.ok(result);
    }


    // 특정 산책로 상세 조회
    @GetMapping("/{trailId}")
    public ResponseEntity<TrailDto> getTrailDetail(@PathVariable Long trailId){

        TrailDto trailDto = trailService.getTrailDetail(trailId);
        return ResponseEntity.ok(trailDto);
    }



    // 산책로 목록 조회 : 난이도(difficultyLevel), 지역명(city_name) 필터링
    // 이름	        타입	    필수	    설명
    //cityName	    String	N	    지역명 (서울, 경기 등)
    //difficulty	String	N	    난이도 (쉬움, 보통, 어려움)
    @GetMapping
    public ResponseEntity<?> findTrails(@RequestParam(required = false) String difficultyLevel,
                                       @RequestParam(required = false) String cityName
    ){
        List<TrailDto> trails = trailService.findTrails(difficultyLevel,cityName);
        return ResponseEntity.ok(trails);
    }



    // 산책로 검색
    @GetMapping("/search")
    public ResponseEntity<?> findTrailsByKeyword(@RequestParam String keyword){

        List<TrailDto> searchTrails = trailService.findTrailsByKeyword(keyword);

        System.out.println("검색 결과 개수: " + searchTrails.size());
        for (TrailDto trail : searchTrails) {
            System.out.println(trail);
        }
        return ResponseEntity.ok(searchTrails);
    }


    // 사용자 위치, 반경 범위를 받아서 근처 산책로 까지의 거리
    // 산책로 거리순 정렬할 때마다 요청
    // param : 사용자 위도, 경도
    // return : 근처 산책로 목록 List<TrailLocationDto>
    @GetMapping("/nearby")
    public ResponseEntity<?> findTrailsNearby(@RequestParam double userLat, @RequestParam double userLong){

        List<TrailWithDistanceDto> trailWithDistances = trailService.findTrailsNearby(userLat, userLong);

        System.out.println("검색 결과 개수: " + trailWithDistances.size());
        for (TrailWithDistanceDto trail : trailWithDistances) {
            System.out.println(trail);
        }

        return ResponseEntity.ok(trailWithDistances);
    }


    // 사용자 위치, 반경 범위를 받아서 근처 산책로 까지의 거리
    // 산책로 거리순 정렬할 때마다 요청
    // param : 사용자 위도, 경도
    // return : 근처 산책로 목록 List<TrailLocationDto>
    @GetMapping("/nearest")
    public ResponseEntity<?> findNearestTrail(@RequestParam double userLat, @RequestParam double userLong){
        try{
            TrailWithDistanceDto trailWithDistance = trailService.findNearestTrail(userLat, userLong);
            return ResponseEntity.ok(trailWithDistance);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
