package com.jup.jupging.global.common.s3.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jup.jupging.domain.member.service.IMemberService;
import com.jup.jupging.global.common.oauth2.JwtUtil;
import com.jup.jupging.global.common.s3.service.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class ImageUploadController {

	private final S3Uploader s3Uploader;
	private final IMemberService memberService;
	private final JwtUtil jwtUtil;
	
	private Long memberIdFrom(String authHeader) {
		 String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		 if (!jwtUtil.validateToken(token)) {
			 throw new IllegalArgumentException("invalid token");
		 }
		 return jwtUtil.getMemberId(token);
	 }

	@PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String upload(@RequestPart("image") MultipartFile multipartFile) throws IOException {
		return s3Uploader.upload(multipartFile, "static");
	}
	
	@GetMapping("/images/delete")
	public String delete(@RequestParam("fileUrl") String fileUrl) {
		s3Uploader.delete(fileUrl);
		return "good";
	}
	
	@PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> insertProfileImage(@RequestHeader(value = "Authorization", required = false) String authHeader,
			@RequestPart("image") MultipartFile multipartFile) throws IOException {
		String imageUrl = s3Uploader.upload(multipartFile, "static");
		Long memberId = memberIdFrom(authHeader);
		if (imageUrl == null) {
			return ResponseEntity.notFound().build(); 
		}
		memberService.updateProfileImageKey(memberId, imageUrl);
		return ResponseEntity.ok(imageUrl);
	}
	
	@PutMapping("/profile")
	public ResponseEntity<String> updateProfileImage(@RequestParam("image") MultipartFile multipartFile, 
			@RequestParam("imageUrl") String imageUrl) {
		try {
			String newImageUrl = s3Uploader.upload(multipartFile, "static");
			if (newImageUrl == null) {
				return ResponseEntity.notFound().build(); 
			}
			s3Uploader.delete(imageUrl);
			return ResponseEntity.ok(imageUrl);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@DeleteMapping("/profile")
	public ResponseEntity<Void> deleteProfileImage(@RequestParam("imageUrl") String imageUrl) {
		try {
			s3Uploader.delete(imageUrl);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
