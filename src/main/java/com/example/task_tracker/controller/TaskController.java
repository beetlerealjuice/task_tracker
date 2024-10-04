package com.example.task_tracker.controller;

import com.example.task_tracker.dto.TaskDto;
import com.example.task_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

    @RestController
    @RequestMapping("/api/v1/tasks")
    @RequiredArgsConstructor
    public class TaskController {

        private final TaskService taskService;

        @GetMapping
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
        public Flux<TaskDto> getAllTasks() {
            return taskService.findAll();
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
        public Mono<ResponseEntity<TaskDto>> getTaskById(@PathVariable String id) {
            return taskService.findById(id)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskDto>> createTask(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskDto>> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
