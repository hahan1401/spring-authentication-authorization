package com.first_project.demo.service;

import com.first_project.demo.Entity.Permission;
import com.first_project.demo.dto.request.PermissionRequest;
import com.first_project.demo.dto.response.PermissionResponse;
import com.first_project.demo.mapper.PermissionMapper;
import com.first_project.demo.repository.PermissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

  private final PermissionRepository permissionRepository;

  private final PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest request) {
    Permission permission = permissionMapper.toPermission(request);
    permission = permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  public List<PermissionResponse> getAll() {
    List<Permission> permissions = permissionRepository.findAll();
    List<PermissionResponse> permissionResponses = permissions.stream().map(
        permissionMapper::toPermissionResponse).toList();
    return permissionResponses;
  }

  public boolean delete(String permissionName) {
    permissionRepository.deleteById(permissionName);
    return true;
  }
}
