package com.first_project.demo.controller;

import com.first_project.demo.Entity.Role;
import com.first_project.demo.dto.request.RoleRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.response.RoleResponse;
import com.first_project.demo.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
  private final RoleService roleService;

//  @PreAuthorize("hasRole('ADMIN')")
  @PreAuthorize("hasAuthority('UPDATE_DATA')")
  @PostMapping
  public ApiResponse<RoleResponse> create(@RequestBody  RoleRequest request) {
    RoleResponse role = roleService.create(request);
    return ApiResponse.<RoleResponse>builder().responseData(role).build();
  }

  @GetMapping
  public ApiResponse<List<RoleResponse>> getAll() {
    List<RoleResponse> roles = roleService.getAll();
    return ApiResponse.<List<RoleResponse>>builder().responseData(roles).build();
  }

  @DeleteMapping("{roleName}")
  public ApiResponse<Boolean> deleteRole(@PathVariable String roleName) {
    boolean isSuccess = roleService.deleteOne(roleName);
    return ApiResponse.<Boolean>builder().responseData(isSuccess).build();
  }
}
