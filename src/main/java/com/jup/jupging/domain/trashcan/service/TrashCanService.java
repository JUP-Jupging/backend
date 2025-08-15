package com.jup.jupging.domain.trashcan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jup.jupging.domain.trashcan.dto.TrashCanAPI;
import com.jup.jupging.domain.trashcan.dto.TrashCanDto;
import com.jup.jupging.domain.trashcan.entity.TrashCan;
import com.jup.jupging.domain.trashcan.mapper.TrashCanMapper;
import com.jup.jupging.domain.trashcan.repository.TrashCanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrashCanService {

    private final TrashCanRepository trashCanRepository;

    public TrashCanService(TrashCanRepository trashCanRepository) {
        this.trashCanRepository = trashCanRepository;
    }

    // 휴지통 약 2300개
    public void insertTrashCans() throws IOException, InterruptedException {

        String encodeKey = "D0ttJITkgOKeuA%2FnPRQo3iEM%2BXCCsx93FD6D%2BVmRXcOQLLoxZv2BafA72DRdoRWYBtHKyJHa3MWMBRt9ud6LYA%3D%3D";
        int pageNo = 1;
        int numOfRows = 100;
        int totalCount=0; // 최대값 설정

        while (true) {

            String fullUrl = "http://api.data.go.kr/openapi/tn_pubr_public_trash_can_api?serviceKey=" + encodeKey
                    + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&type=json";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            // JSON → DTO 매핑
            ObjectMapper mapper = new ObjectMapper();
            TrashCanAPI trashCanAPI = mapper.readValue(response.body(), TrashCanAPI.class);
            System.out.println(trashCanAPI);

            List<TrashCanAPI.Item> items = trashCanAPI.getResponse().getBody().getItems();
            if (items == null || items.isEmpty()) {
                break;
            }

            List<TrashCan> entities = items.stream()
                    .map(item -> {
                        TrashCan trashCan = new TrashCan();
                        trashCan.setPlaceName(item.getInstlPlcNm());
                        trashCan.setCityName(item.getCtpvNm());
                        trashCan.setDistrictName(item.getSggNm());
                        trashCan.setRoadAddress(item.getLctnRoadNm());
                        trashCan.setLotNumberAddress(item.getLctnLotnoAddr());

                        try {
                            if (item.getLat() != null && !item.getLat().isEmpty()) {
                                trashCan.setLatitude(Double.parseDouble(item.getLat()));
                            }
                        } catch (NumberFormatException e) {
                            trashCan.setLatitude(0.0);
                        }

                        try {
                            if (item.getLot() != null && !item.getLot().isEmpty()) {
                                trashCan.setLongitude(Double.parseDouble(item.getLot()));
                            }
                        } catch (NumberFormatException e) {
                            trashCan.setLongitude(0.0);
                        }

                        trashCan.setTrashCanType(item.getTrashCanKnd());
                        trashCan.setInstitution(item.getMngInstNm());
                        trashCan.setInstitutionTel(item.getMngInstTelno());
                        return trashCan;
                    })
                    .collect(Collectors.toList());

            trashCanRepository.saveAll(entities);
            System.out.println("페이지 " + pageNo + " 저장 완료");
            pageNo++;  // 지역변수 pageNo 증감

        }
    }


    // 휴지통 상세 조회
    @Transactional
    public TrashCanDto getTrashCanDetail(Long trashCanId) {
        TrashCan trashCan = trashCanRepository.findById(trashCanId).orElseThrow(
                () -> new IllegalArgumentException("해당 휴지통이 존재하지 않습니다. ID: " + trashCanId));

        return TrashCanMapper.toDto(trashCan);
    }


//     모든 휴지통 조회
    public List<TrashCanDto> findTrashCans() {

        List<TrashCan> trashCans = trashCanRepository.findAll();

        return trashCans.stream()
                .map(TrashCanMapper::toDto)   // 각 TrashCan을 DTO로 변환
                .collect(Collectors.toList()); // 결과를 List로 수집
    }
}
