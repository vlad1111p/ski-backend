package com.skitrainer.controller;

import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    @PutMapping("/{id}/role")
    public User updateRole(@PathVariable final Long id, @RequestParam final User.Role role) {
        final User user = userRepository.findById(id).orElseThrow();
        user.setRole(role);
        return userRepository.save(user);
    }
}
