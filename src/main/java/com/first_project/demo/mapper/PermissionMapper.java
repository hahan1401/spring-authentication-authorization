package com.first_project.demo.mapper;

import com.first_project.demo.Entity.Permission;
import com.first_project.demo.dto.request.PermissionRequest;
import com.first_project.demo.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);
  PermissionResponse toPermissionResponse(Permission permission);
}
