package com.tsonline.app.common.util;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@Primary
@RequiredArgsConstructor
public class S3ServiceImpl implements FileService {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Override
	public String uploadFileName(MultipartFile file) throws IOException {
		// Generate unique File Name
		String originalFileName = file.getOriginalFilename();
		String randomUUID = UUID.randomUUID().toString();
		String fileName = randomUUID + originalFileName.substring(originalFileName.lastIndexOf("."));

		// Upload to S3
		try {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(fileName)
					.contentType(file.getContentType())
					.build();

			s3Client.putObject(putObjectRequest, 
					RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

			return fileName;
		} catch (IOException e) {
			throw new RuntimeException("Fail to upload file to S3");
		}
	}

	@Override
	public String getPresignedUrl(String fileName) {
		if (fileName == null || fileName.isEmpty()) return null;
		
		// Create temporary 15 min link to use at frontend
		try (S3Presigner presigner = S3Presigner.create()) {
			GetObjectRequest getObjectRequest = GetObjectRequest
					.builder()
					.bucket(bucketName)
					.key(fileName)
					.build();
			
			GetObjectPresignRequest presignRequest = GetObjectPresignRequest
					.builder()
					.signatureDuration(Duration.ofMinutes(15))
					.getObjectRequest(getObjectRequest)
					.build();
			
			return presigner.presignGetObject(presignRequest).url().toString();
		}
	}

	@Override
	public void deleteFile(String fileName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
	}
}
