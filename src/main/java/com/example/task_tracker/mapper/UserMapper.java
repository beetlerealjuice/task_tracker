package com.example.task_tracker.mapper;

import com.example.task_tracker.dto.UserDto;
import com.example.task_tracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);

}
