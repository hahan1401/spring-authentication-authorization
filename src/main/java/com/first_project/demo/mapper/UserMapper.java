package com.first_project.demo.mapper;

import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.UserCreationRequest;
import com.first_project.demo.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
}
