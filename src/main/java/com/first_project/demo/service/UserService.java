package com.first_project.demo.service;

import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.UserCreationRequest;
import com.first_project.demo.dto.response.UserResponse;
import com.first_project.demo.exception.AppException;
import com.first_project.demo.exception.ErrorCode;
import com.first_project.demo.mapper.UserMapper;
import com.first_project.demo.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  public User createUSer(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(request);

    return userRepository.save(user);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponse> getUsers() {
    List<User> users = userRepository.findAll();

    List<UserResponse> usersResponse;

    usersResponse = users.stream().map(userMapper::toUserResponse)
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

    return userMapper.toUserResponse((userRepository.save(user)));
  }

  public boolean deleteUser(String id) {
    userRepository.deleteById(id);
    return true;
  }
}
