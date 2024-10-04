package com.example.task_tracker.service;

import com.example.task_tracker.dto.TaskDto;
import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.exceptions.EntityNotFoundException;
import com.example.task_tracker.mapper.TaskMapper;
import com.example.task_tracker.mapper.UserMapper;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Flux<TaskDto> findAll() {
        return taskRepository.findAll()
                .flatMap(this::mapTaskToDto);
    }

    public Mono<TaskDto> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::mapTaskToDto);
    }

    private Mono<TaskDto> mapTaskToDto(Task task) {
        Mono<User> author = userRepository.findById(task.getAuthorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author ID not found")));

        Mono<User> assignee = userRepository.findById(task.getAssigneeId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Assignee ID not found")));

        Flux<User> observers = Flux.fromIterable(task.getObserverIds())
                .flatMap(userRepository::findById)
                .switchIfEmpty(Flux.error(new EntityNotFoundException("Observers ID not found")));

        return Mono.zip(author, assignee, observers.collectList())
                .map(tuple -> {
                    TaskDto dto = taskMapper.toDto(task);
                    dto.setAuthor(userMapper.toDto(tuple.getT1()));
                    dto.setAssignee(userMapper.toDto(tuple.getT2()));
                    dto.setObservers(tuple.getT3().stream().map(userMapper::toDto)
                            .collect(Collectors.toList()));
                    return dto;
                });
    }



    public Mono<TaskDto> create(TaskDto taskDto) {

        // Находим автора
        Mono<User> authorMono = userRepository.findById(taskDto.getAuthorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author ID not found")));

        // Находим исполнителя
        Mono<User> assigneeMono = userRepository.findById(taskDto.getAssigneeId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Assignee ID not found")));

        // Находим наблюдателей
        Flux<User> observersFlux = Flux.fromIterable(taskDto.getObserverIds())
                .flatMap(userRepository::findById)
                .switchIfEmpty(Flux.error(new EntityNotFoundException("Observers ID not found")));

        // Объединяем результаты и создаем задачу
        return Mono.zip(authorMono, assigneeMono, observersFlux.collectList())
                .flatMap(tuple -> {
                    User author = tuple.getT1();
                    User assignee = tuple.getT2();
                    Set<User> observers = new HashSet<>(tuple.getT3());

                    // Создаем задачу
                    Task task = Task.builder()
                            .id(UUID.randomUUID().toString())
                            .name(taskDto.getName())
                            .description(taskDto.getDescription())
                            .createAt(Instant.now())
                            .updateAt(Instant.now())
                            .status(taskDto.getStatus())
                            .authorId(taskDto.getAuthorId())
                            .assigneeId(taskDto.getAssigneeId())
                            .observerIds(taskDto.getObserverIds())
                            .author(author)
                            .assignee(assignee)
                            .observers(observers)
                            .build();

                    // Сохраняем задачу в репозитории
                    return taskRepository.save(task)
                            .map(taskMapper::toDto);
                });
    }

    public Mono<TaskDto> update(String taskId, TaskDto taskDto) {
        return taskRepository.findById(taskId)
                .flatMap(existingTask -> {
                    // Обновляем поля задачи
                    existingTask.setName(taskDto.getName());
                    existingTask.setDescription(taskDto.getDescription());
                    existingTask.setStatus(taskDto.getStatus());
                    existingTask.setUpdateAt(Instant.now());

                    // Если нужно обновить автора
                    if (!existingTask.getAuthorId().equals(taskDto.getAuthorId())) {
                        existingTask.setAuthorId(taskDto.getAuthorId());
                    }

                    // Если нужно обновить исполнителя и наблюдателей
                    if (!existingTask.getAssigneeId().equals(taskDto.getAssigneeId())) {
                        existingTask.setAssigneeId(taskDto.getAssigneeId());
                    }

                    // Обновляем список наблюдателей
                    existingTask.setObserverIds(taskDto.getObserverIds());

                    // Сохраняем обновлённую задачу
                    return taskRepository.save(existingTask)
                            .flatMap(this::mapTaskToDto);
                });
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

}
