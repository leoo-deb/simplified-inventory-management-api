package com.leo.estoque_api.dto.user;

import com.leo.estoque_api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserDTO(User user);

    @Mapping(target = "user.emailVerified", ignore = true)
    @Mapping(target = "user.active", ignore = true)
    @Mapping(target = "user.createdAt", ignore = true)
    User toUser(UserRequest userRequest);

}
