package com.first_project.demo.service;

import com.first_project.demo.Entity.Permission;
import com.first_project.demo.Entity.Role;
import com.first_project.demo.dto.request.RoleRequest;
import com.first_project.demo.dto.response.RoleResponse;
import com.first_project.demo.mapper.RoleMapper;
import com.first_project.demo.repository.PermissionRepository;
import com.first_project.demo.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;
  private final PermissionRepository permissionRepository;

  public RoleResponse create(RoleRequest request) {
    Role role = new Role();
    role = roleMapper.toRole(request);

    List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

    role.setPermissions(new HashSet<>(permissions));

    return roleMapper.toRoleResponse(roleRepository.save(role));
  }

  public List<RoleResponse> getAll() {
    List<Role> roles = roleRepository.findAll();
    List<RoleResponse> roleResponses = roles.stream().map(roleMapper::toRoleResponse).toList();
    return roleResponses;
  }

  public boolean deleteOne(String roleName) {
    roleRepository.deleteById(roleName);
    return true;
  }
}
