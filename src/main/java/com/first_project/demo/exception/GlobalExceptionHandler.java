package com.first_project.demo.exception;

import com.first_project.demo.dto.response.ApiResponse;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException() {
//        ApiResponse exceptionResponse = new ApiResponse();
//        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        exceptionResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//        exceptionResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        return ResponseEntity.badRequest().body(exceptionResponse);
//    }


  @ExceptionHandler(value = AppException.class)
  ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();

    log.info("asdasdasdsadsadas=================");

    ApiResponse exceptionResponse = new ApiResponse();
    exceptionResponse.setCode(errorCode.getCode());
    exceptionResponse.setMessage(exception.getMessage());
    return ResponseEntity.status(errorCode.getStatusCode()).body(exceptionResponse);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    String enumkey = exception.getFieldError().getDefaultMessage();

    ErrorCode errorCode;

    try {
      errorCode = ErrorCode.valueOf(enumkey);
    } catch (IllegalArgumentException e) {
      errorCode = ErrorCode.INVALID_KEY;
    }

    ApiResponse exceptionResponse = new ApiResponse();
    exceptionResponse.setMessage(errorCode.getMessage());
    exceptionResponse.setCode(errorCode.getCode());
    return ResponseEntity.badRequest().body(exceptionResponse);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }
}
