package com.backend.gym.shared.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.gym.shared.exception.auth.InvalidCredentialsException;
import com.backend.gym.shared.exception.auth.InvalidTokenException;
import com.backend.gym.shared.exception.auth.InvalidVerificationCodeException;
import com.backend.gym.shared.exception.auth.VerificationCodeExpiredException;
import com.backend.gym.shared.exception.domain.AlreadyExistsException;
import com.backend.gym.shared.exception.domain.BusinessException;
import com.backend.gym.shared.exception.domain.NotFoundException;
import com.backend.gym.shared.exception.storage.FileUploadException;
import com.backend.gym.shared.exception.user.UserNotVerifiedException;

import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentials(InvalidCredentialsException ex) {
        return new ErrorResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidToken(InvalidTokenException ex) {
        return new ErrorResponse("INVALID_TOKEN", ex.getMessage());
    }
    
    @ExceptionHandler({InvalidVerificationCodeException.class, VerificationCodeExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleVerificationCode(BusinessException ex) {
        return new ErrorResponse("UNAUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUserNotVerified(UserNotVerifiedException ex) {
        return new ErrorResponse("FORBIDDEN", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExists(AlreadyExistsException ex) {
        return new ErrorResponse("CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileUpload(FileUploadException ex) {
        return new ErrorResponse("FILE_UPLOAD_ERROR", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        ex.printStackTrace();
        return new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
    }

    public record ErrorResponse(String code, String message) {}
}