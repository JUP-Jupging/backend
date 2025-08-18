package com.jup.jupging.global.common.s3.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jup.jupging.global.common.s3.service.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class ImageUploadController {

	private final S3Uploader s3Uploader;
	
	@PostMapping("/profile")
	public ResponseEntity<String> insertProfileImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
		String imageUrl = s3Uploader.upload(multipartFile, "static");
		if (imageUrl == null) {
			return ResponseEntity.notFound().build(); 
		}
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
