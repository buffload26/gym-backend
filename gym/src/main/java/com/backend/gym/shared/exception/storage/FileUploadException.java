package com.backend.gym.shared.exception.storage;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String message) {
        super(message);
    }
}