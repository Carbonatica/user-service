package com.example.userservice.web.dto.mapper;

import com.example.userservice.model.User;
import com.example.userservice.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
