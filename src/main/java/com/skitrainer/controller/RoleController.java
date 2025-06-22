package com.skitrainer.controller;

import com.skitrainer.model.Role;
import com.skitrainer.model.User;
import com.skitrainer.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/switch")
    public void switchRole(@AuthenticationPrincipal User user, @RequestParam Role newRole) {
        roleService.switchRole(user, newRole);
    }
}