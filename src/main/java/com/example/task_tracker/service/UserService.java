package com.example.task_tracker.service;

import com.example.task_tracker.dto.UserDto;
import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.mapper.UserMapper;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Flux<UserDto> findAll() {
        return userRepository.findAll()
                .map(userMapper::toDto);
    }

    public Mono<UserDto> findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public Mono<UserDto> create(UserDto userDto, RoleType roleType) {
        User user = userMapper.toEntity(userDto);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addRole(roleType);

        return userRepository.save(user)
                .map(userMapper::toDto);
    }

    public Mono<UserDto> update(String id, UserDto userDto) {
        return userRepository.findById(id)
                .flatMap(updateUser -> {
                    if (StringUtils.hasText(userDto.getUsername())) updateUser.setUsername(userDto.getUsername());
                    if (userDto.getEmail() != null) updateUser.setEmail(userDto.getEmail());
                    return userRepository.save(updateUser);
                })
                .map(userMapper::toDto);
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
