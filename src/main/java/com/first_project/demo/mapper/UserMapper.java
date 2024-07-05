package com.first_project.demo.mapper;

import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.UserCreationRequest;
import com.first_project.demo.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target =  "roles", ignore = true)
    User toUser(UserCreationRequest request);

    @Mapping(target =  "roles", ignore = true)
    UserResponse toUserResponse(User user);
}
