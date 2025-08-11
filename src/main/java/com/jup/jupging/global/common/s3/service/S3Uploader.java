package com.jup.jupging.global.common.s3.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 1. S3에 저장될 파일 이름 생성 (중복 방지)
        String s3FileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // 2. 파일 메타데이터 생성 (파일 크기, 타입 등)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // 3. S3에 파일 업로드 (InputStream과 메타데이터 사용)
        amazonS3Client.putObject(bucket, s3FileName, multipartFile.getInputStream(), metadata);
        
        // 4. 업로드된 파일의 S3 URL 주소 반환
        return amazonS3Client.getUrl(bucket, s3FileName).toString();
    }
    
    public void delete(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String key = url.getPath().substring(1); // URL 경로에서 맨 앞의 '/'를 제거합니다.
            
            // deleteObject(버킷이름, 객체키)
            amazonS3Client.deleteObject(bucket, key);
            
        } catch (MalformedURLException e) {
            // 유효하지 않은 URL 형식일 경우 예외 처리
            throw new IllegalArgumentException("유효하지 않은 URL 입니다: " + fileUrl, e);
        }
    }
}
