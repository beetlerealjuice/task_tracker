package com.example.task_tracker.mapper;

import com.example.task_tracker.dto.TaskDto;
import com.example.task_tracker.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskDto toDto(Task task);
    Task toEntity(TaskDto taskDto);
}