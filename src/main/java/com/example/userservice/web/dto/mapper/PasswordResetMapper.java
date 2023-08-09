package com.example.userservice.web.dto.mapper;

import com.example.userservice.model.authorization.PasswordReset;
import com.example.userservice.web.dto.PasswordResetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordResetMapper
        extends Mappable<PasswordReset, PasswordResetDto> {
}
