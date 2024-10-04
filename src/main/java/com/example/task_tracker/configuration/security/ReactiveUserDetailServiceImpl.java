package com.example.task_tracker.configuration.security;

import com.example.task_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return userService.findByUsername(username)
                .map(UserPrincipal::new);
    }


}
