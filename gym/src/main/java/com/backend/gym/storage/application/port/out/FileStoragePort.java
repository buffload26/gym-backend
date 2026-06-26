package com.backend.gym.storage.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {
    String upload(MultipartFile file);
    void delete(String fileName);
    String replaceFile(MultipartFile file, String existingUrl);
}