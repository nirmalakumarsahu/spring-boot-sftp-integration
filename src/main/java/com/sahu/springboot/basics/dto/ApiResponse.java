package com.sahu.springboot.basics.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sahu.springboot.basics.constant.ApiStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    private Integer code;
    private ApiStatus status;
    private String message;
    private String path;
    private T result;
    private Object error;

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus code, String message, T result) {
        return ResponseEntity.status(code).body(new ApiResponse<>(code.value(), ApiStatus.SUCCESS,
                message, getCurrentRequestPath(), result, null)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> failure(HttpStatus code, String message, Object error) {
        return ResponseEntity.status(code).body(new ApiResponse<>(code.value(), ApiStatus.FAILURE,
                message, getCurrentRequestPath(), null, error)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus code, String message, Object error) {
        return ResponseEntity.status(code).body(new ApiResponse<>(code.value(), ApiStatus.ERROR,
                message, getCurrentRequestPath(), null, error)
        );
    }

    private static String getCurrentRequestPath() {
        try {
            return ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        } catch (Exception e) {
            return null;
        }
    }

}