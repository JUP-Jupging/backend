package com.jup.jupging.domain.plogging.controller;

import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingRequestDto;
import com.jup.jupging.domain.plogging.service.PloggingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/zubging/plogging", produces = "application/json; charset=utf8")
public class PloggingController {
    private final PloggingService ploggingService;
    public PloggingController(PloggingService ploggingService) {
        this.ploggingService = ploggingService;
    }

    /**
     * FormData로 받음 (ModelAttribute)
     * 플로깅 시작할 때 보내야할 것
     *     private Long memberId;
     *     private Long trailId;
     *     private boolean isStart;
     *     private Double startLat;
     *     private Double startLng;
     * 플로깅 끝날 때 보내야할 것
     *     private Long memberId;
     *     private Long trailId;
     *     private boolean isStart;
     *     private String ploggingTime;
     *     private Double distance;
     * JPA 비관적 락 걸어서 빠르게 눌러 중복 시작할 수 있는 버그 막음
     * 시나리오 :
     * 진행 중인 플로깅 있을 때 start 요청 : 이미 플로깅 중입니다. 메세지 반환
     * 진행 중인 플로깅 없을 때 start 요청 : 새 레코드 생성
     * 진행 중인 플로깅 있을 때 end 요청 : 레코드 기록된 데이터 (거리 등) 추가하고 마감
     * 진행 중인 플로깅 없을 때 end 요청 : 진행 중인 플로깅이 없습니다. 메세지 반환
     * @param plogging
     * @return
     */
    @PostMapping
    public ResponseEntity<?> insertPlogging(@RequestBody PloggingRequestDto plogging){
        try{
            System.out.println("============"+plogging.isStart());
            System.out.println(plogging.getMemberId());

            ploggingService.insertPlopping(plogging);
            return ResponseEntity.ok().body(plogging.isStart() ? "기록을 시작합니다." : "기록이 완료되었어요.");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/{ploggingId}/reports/{reportId}/pick")
    public String insertPickedTrash(@PathVariable Long ploggingId, @PathVariable Long reportId){

        String result = ploggingService.insertPickedTrash(ploggingId, reportId);

        return null;
    }
    @GetMapping("/{ploggingId}")
    public ResponseEntity<PloggingDto> getPlopping(@PathVariable Long ploggingId, @RequestParam Long memberId){
        return ResponseEntity.ok().body(ploggingService.getPlogging(ploggingId, memberId));
    }


}
