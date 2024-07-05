package com.first_project.demo.service;

import com.first_project.demo.Entity.Role;
import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.UserCreationRequest;
import com.first_project.demo.dto.response.RoleResponse;
import com.first_project.demo.dto.response.UserResponse;
import com.first_project.demo.exception.AppException;
import com.first_project.demo.exception.ErrorCode;
import com.first_project.demo.mapper.RoleMapper;
import com.first_project.demo.mapper.UserMapper;
import com.first_project.demo.repository.RoleRepository;
import com.first_project.demo.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  public User createUSer(UserCreationRequest request) throws AppException {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    List<Role> roles = roleRepository.findAllById(request.getRoles());
    User user = userMapper.toUser(request);
    user.setRoles(new HashSet<Role>(roles));

    return userRepository.save(user);
  }

  public List<UserResponse> getUsers() {
    List<User> users = userRepository.findAll();

    List<UserResponse> usersResponse;

    usersResponse = users.stream().map(user -> {
          UserResponse userResponse = userMapper.toUserResponse(user);
          List<RoleResponse> roleResponses = user.getRoles().stream().map(roleMapper::toRoleResponse).toList();
          userResponse.setRoles(new HashSet<RoleResponse>(roleResponses));
          return userResponse;
        })
        .collect(Collectors.toList());

    return usersResponse;
  }

  public UserResponse getUser(String id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String id, UserCreationRequest request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    List<Role> roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<Role>(roles));

    return userMapper.toUserResponse((userRepository.save(user)));
  }

  public boolean deleteUser(String id) {
    userRepository.deleteById(id);
    return true;
  }
}
