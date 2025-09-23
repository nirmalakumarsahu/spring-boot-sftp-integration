package com.sahu.springboot.basics.exception;

import com.sahu.springboot.basics.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SftpConfigNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleSftpConfigNotFoundException(SftpConfigNotFoundException sftpConfigNotFoundException)
    {
        return buildErrorResponse(HttpStatus.NOT_FOUND, sftpConfigNotFoundException.getMessage(), null);
    }

    @ExceptionHandler(SftpConfigAlreadyExistException.class)
    public ResponseEntity<ApiResponse<String>> handleSftpConfigAlreadyExistException(SftpConfigAlreadyExistException sftpConfigAlreadyExistException)
    {
        return buildErrorResponse(HttpStatus.CONFLICT, sftpConfigAlreadyExistException.getMessage(), null);
    }

    @ExceptionHandler(InvalidSftpKeyFileException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidSftpKeyFileException(InvalidSftpKeyFileException invalidSftpKeyFileException)
    {
        return buildErrorResponse(HttpStatus.NOT_ACCEPTABLE, invalidSftpKeyFileException.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException)
    {
        Map<String, String> fieldErrors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse("Invalid Values")
                ));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    @ExceptionHandler(SftpException.class)
    public ResponseEntity<ApiResponse<String>> handleSftpException(SftpException exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null);
    }

    private ResponseEntity<ApiResponse<String>> buildErrorResponse(HttpStatus httpStatus, String message, Object error) {
        return ApiResponse.error(
                httpStatus,
                message,
                error
        );
    }

}
