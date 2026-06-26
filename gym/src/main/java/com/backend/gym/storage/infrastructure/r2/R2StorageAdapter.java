package com.backend.gym.storage.infrastructure.r2;

import com.backend.gym.shared.exception.storage.FileUploadException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class R2StorageAdapter implements FileStoragePort {

    private final S3Client r2Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Override
    public String upload(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

            r2Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return publicUrl + "/" + fileName;

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

            r2Client.deleteObject(request);
        } catch (Exception e) {
            throw new FileUploadException("Failed to delete file: " + fileName);
        }
    }

    @Override
    public String replaceFile(MultipartFile file, String existingUrl) {
        if (existingUrl == null || !existingUrl.startsWith(publicUrl)) {
            return upload(file);
        }

        try {
            String objectKey = existingUrl.replace(publicUrl + "/", "");

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .build();

            r2Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return existingUrl;
            
        } catch (IOException e) {
            throw new FileUploadException("Failed to replace file: " + existingUrl);
        }
    }
}