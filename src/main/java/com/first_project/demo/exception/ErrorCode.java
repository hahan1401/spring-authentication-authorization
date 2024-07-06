package com.first_project.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
  INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
  USER_NOT_EXIST(1005, "User not exist", HttpStatus.NOT_FOUND),
  UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1007, "Unauthorized", HttpStatus.FORBIDDEN),
  INVALID_DOB(1004, "Your age must be at least {min}", HttpStatus.BAD_REQUEST);

  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;
}
