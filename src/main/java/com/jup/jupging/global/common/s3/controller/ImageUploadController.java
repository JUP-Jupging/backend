package com.jup.jupging.global.common.s3.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jup.jupging.global.common.s3.service.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageUploadController {
	private final S3Uploader s3Uploader;

	@PostMapping("/images")
	public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
		return s3Uploader.upload(multipartFile, "static");
	}
	
	@GetMapping("/images/delete")
	public String delete(@RequestParam("fileUrl") String fileUrl) {
		s3Uploader.delete(fileUrl);
		return "good";
	}
}
