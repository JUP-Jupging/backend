package com.jup.jupging.domain.plogging.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jup.jupging.domain.plogging.dto.PloggingDto;
import com.jup.jupging.domain.plogging.service.IPloggingService;
import com.jup.jupging.global.oauth.jwt.JwtUtil;


@RestController
public class PloggingController {

	@Autowired
	IPloggingService ploggingService;
	
	private JwtUtil jwtUtil;
	
	private Long memberIdFrom(String authHeader) {
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7) : null;
        if (token == null || !jwtUtil.isValid(token)) {
            throw new IllegalArgumentException("invalid token");
        }
        return jwtUtil.getMemberId(token); // subject를 Long으로 반환
    }
	
	@GetMapping("/plogging/me")
	public ResponseEntity<?> myReports(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long memberId = 1L;
//        if (memberId == null) {
//            return ResponseEntity.status(401).body("unauthorized");
//        }

        List<PloggingDto> list = ploggingService.findMyPlogging(memberId);
        return ResponseEntity.ok(list);
    }
}
