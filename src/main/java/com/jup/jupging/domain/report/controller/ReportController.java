package com.jup.jupging.domain.report.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jup.jupging.domain.report.dto.ReportReq;
import com.jup.jupging.domain.report.dto.ReportSummaryDto;
import com.jup.jupging.domain.report.service.IReportService;
import com.jup.jupging.global.common.oauth2.JwtUtil;
import com.jup.jupging.global.common.s3.service.S3Uploader;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
public class ReportController {
	
	private final IReportService reportService;
    private final JwtUtil jwtUtil;
    private final S3Uploader s3Uploader;

    private Long memberIdFrom(String authHeader) {
        String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("invalid token");
        }
        return jwtUtil.getMemberId(token); // subject를 Long으로 반환
    }

    @PostMapping(value = "/reports", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReport(@RequestPart("image") MultipartFile multipartFile, 
    									  @RequestHeader(value = "Authorization", required = false) String authHeader,
                                          @RequestBody ReportReq req) throws IOException {
        try {
            Long memberId = memberIdFrom(authHeader);     // ← 토큰에서 memberId 추출
            String imageUrl = s3Uploader.upload(multipartFile, "static");
            req.setImageUrl(imageUrl);
            reportService.insertReport(req, memberId);    // ← 서비스가 DB에 삽입
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
        }
    }
    
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<?> detail(@PathVariable("reportId") Long reportId) {
        var dto = reportService.findReportDetail(reportId);
        return (dto == null)
                ? ResponseEntity.status(404).body("report not found")
                : ResponseEntity.ok(dto);
    }
    
    
    
    @GetMapping("/reports/me")
    public ResponseEntity<?> myReports(@RequestHeader(value = "Authorization", required = false) String authHeader) {

    	Long memberId = memberIdFrom(authHeader);
//        if (memberId == null) {
//            return ResponseEntity.status(401).body("unauthorized");
//        }

        List<ReportSummaryDto> list = reportService.findMyReports(memberId);
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/reports/me/{reportId}")
    public ResponseEntity<?> myReportDetail(@RequestHeader(value = "Authorization", required = false) String authHeader
    		, @PathVariable("reportId") Long reportId){
    	Long memberId = memberIdFrom(authHeader);
    	var dto = reportService.findMyReportDetail(reportId, memberId);
    	return(dto == null) ? ResponseEntity.status(404).body("report not found") : ResponseEntity.ok(dto); 
    }
    
    
    
    @PutMapping("/reports/{reportId}")
    public ResponseEntity<?> pick(@PathVariable("reportId") Long reportId) {
        return reportService.pickReport(reportId)
                ? ResponseEntity.ok("picked")
                : ResponseEntity.status(404).body("report not found");
    }
    
}
