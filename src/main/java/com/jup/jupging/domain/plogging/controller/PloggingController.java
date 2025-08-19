package com.jup.jupging.domain.plogging.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jup.jupging.domain.plogging.dto.MyPloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.dto.PloggingInsertRequestDto;
import com.jup.jupging.domain.plogging.dto.PloggingRequestDto;
import com.jup.jupging.domain.plogging.service.IPloggingService;
import com.jup.jupging.domain.plogging.service.PloggingService;
import com.jup.jupging.global.common.oauth2.JwtUtil;
import com.jup.jupging.global.common.s3.service.S3Uploader;

@RestController
@RequestMapping("/plogging")
public class PloggingController {
    private final PloggingService ploggingService;
    public PloggingController(PloggingService ploggingService) {
        this.ploggingService = ploggingService;
    }
    @Autowired
    IPloggingService ploggingService2;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    S3Uploader s3Uploader;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> insertPlogging(@RequestHeader(value = "Authorization", required = false) String authHeader, 
    		@RequestPart(value = "image", required = false) MultipartFile multipartFile,
    		PloggingInsertRequestDto ploggingInsertRequestDto) {
		try {
			String imageUrl = s3Uploader.upload(multipartFile, "static");
			Long memberId = this.memberIdFrom(authHeader);
			ploggingInsertRequestDto.setMemberId(memberId);
			ploggingInsertRequestDto.setImageUrl(imageUrl);
			ploggingService2.insertPlogging(ploggingInsertRequestDto);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
    	return ResponseEntity.ok().build();
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
    @PostMapping("/record")
    public ResponseEntity<?> insertPlogging(@RequestBody PloggingRequestDto plogging, 
    		@RequestPart(value = "image", required = false) MultipartFile multipartFile){
        try{
            ploggingService.insertPlopping(plogging, multipartFile);
            return ResponseEntity.ok().body(plogging.isStart() ? "기록을 시작합니다." : "기록이 완료되었어요.");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{ploggingId}/reports/{reportId}/pick")
    public String insertPickedTrash(@PathVariable Long ploggingId, @PathVariable Long reportId){
        String result = ploggingService.insertPickedTrash(ploggingId, reportId);
        return result;
    }
    
    @GetMapping("/{ploggingId}/members/{memberId}")
    public ResponseEntity<PloggingDto> getPlopping(@PathVariable Long ploggingId, @PathVariable Long memberId){
        return ResponseEntity.ok().body(ploggingService.getPlogging(ploggingId, memberId));
    }

    @GetMapping("/trail/count/{trailId}")
    public ResponseEntity<Long> getPloggingCountByTrail(@PathVariable Long trailId){
        return ResponseEntity.ok().body(ploggingService.getPloggingCountByTrail(trailId));
    }

	private Long memberIdFrom(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7) : null;
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("invalid token");
        }
        return jwtUtil.getMemberId(token); // subject를 Long으로 반환
    }
	
	@GetMapping("/me")
	public ResponseEntity<?> myReports(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		Long memberId = this.memberIdFrom(authHeader);
        if (memberId == null) {
            return ResponseEntity.status(401).body("unauthorized");
        }
        List<MyPloggingDto> list = ploggingService2.findMyPlogging(memberId);
        return ResponseEntity.ok(list);
    }
}
