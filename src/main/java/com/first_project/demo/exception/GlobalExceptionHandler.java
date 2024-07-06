package com.first_project.demo.exception;

import com.first_project.demo.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//  @ExceptionHandler(value = AppException.class)
//  ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception) {
//    ErrorCode errorCode = exception.getErrorCode();
//
//    log.info("asdasdasdsadsadas=================");
//
//    ApiResponse exceptionResponse = new ApiResponse();
//    exceptionResponse.setCode(errorCode.getCode());
//    exceptionResponse.setMessage(exception.getMessage());
//    return ResponseEntity.status(errorCode.getStatusCode()).body(exceptionResponse);
//  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

    ErrorCode errorCode;

    ConstraintViolation constraintViolation = exception.getBindingResult().getAllErrors()
        .getFirst().unwrap(ConstraintViolation.class);

    Map attributes = constraintViolation.getConstraintDescriptor().getAttributes();

    try {
      errorCode = ErrorCode.valueOf(enumKey);
    } catch (IllegalArgumentException e) {
      errorCode = ErrorCode.INVALID_KEY;
    }

    ApiResponse exceptionResponse = new ApiResponse();
    exceptionResponse.setMessage(
        Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes)
            : errorCode.getMessage());
    exceptionResponse.setCode(errorCode.getCode());
    return ResponseEntity.badRequest().body(exceptionResponse);
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }

  private String mapAttribute(String message, Map<String, Object> attrributes) {
    String MIN_ATTRIBUTE = "min";
    String minValue = String.valueOf(attrributes.get(MIN_ATTRIBUTE));

    return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
  }
}
