package com.first_project.demo.controller;

import com.first_project.demo.dto.request.PermissionRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.response.PermissionResponse;
import com.first_project.demo.service.PermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

  private final PermissionService permissionService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping()
  ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
    return ApiResponse.<PermissionResponse>builder().responseData(permissionService.create(request))
        .build();
  }

  @GetMapping
  ApiResponse<List<PermissionResponse>> getAll() {
    return ApiResponse.<List<PermissionResponse>>builder().responseData(permissionService.getAll())
        .build();
  }

  @DeleteMapping("/{permissionName}")
  ApiResponse<Boolean> deleteByName(@PathVariable String permissionName) {
    return ApiResponse.<Boolean>builder().responseData(permissionService.delete(permissionName))
        .build();
  }
}
