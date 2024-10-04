package com.example.task_tracker.controller;

import com.example.task_tracker.dto.UserDto;
import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<UserDto> getAllUsers()
    {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto userDto,
                                                    @RequestParam(value = "role") RoleType roleType) {
        return userService.create(userDto, roleType)
                .map(ResponseEntity::ok);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
