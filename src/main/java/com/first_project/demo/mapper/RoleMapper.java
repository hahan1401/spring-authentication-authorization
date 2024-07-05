package com.first_project.demo.mapper;

import com.first_project.demo.Entity.Role;
import com.first_project.demo.dto.request.RoleRequest;
import com.first_project.demo.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target =  "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toRoleResponse(Role role);
}
