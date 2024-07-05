package com.first_project.demo.controller;

import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.UserCreationRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.response.UserResponse;
import com.first_project.demo.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping()
  ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
    ApiResponse<User> userResponse = new ApiResponse<User>();
    userResponse.setResponseData(userService.createUSer(request));
    return userResponse;
  }

  @GetMapping
  ApiResponse<List<UserResponse>> getUsers() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    authentication.getAuthorities()
        .forEach(grantedAuthority -> log.info("111111111111:   {}",
            grantedAuthority.getAuthority()));

    return ApiResponse.<List<UserResponse>>builder().responseData(userService.getUsers()).build();
  }

  @GetMapping("/{id}")
  ApiResponse<UserResponse> getUSer(@PathVariable("id") String id) {
    UserResponse user = userService.getUser(id);

    return ApiResponse.<UserResponse>builder().responseData(user).build();
  }

  @PutMapping("/{id}")
  UserResponse updateUser(@PathVariable("id") String id, @RequestBody UserCreationRequest request) {
    return userService.updateUser(id, request);
  }

  @DeleteMapping("{id}")
  boolean deleteUser(@PathVariable String id) {
    return userService.deleteUser(id);
  }
}
