package com.backend.gym.shared.exception.storage;

import com.backend.gym.shared.exception.domain.BusinessException;

public class FileUploadException extends BusinessException {
    public FileUploadException(String message) {
        super(message);
    }
}