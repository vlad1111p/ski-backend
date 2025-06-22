package com.skitrainer.service.role.impl;

import com.skitrainer.model.Role;
import com.skitrainer.model.User;
import com.skitrainer.service.role.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public void switchRole(User user, Role newRole) {
        if (user.getRoles().contains(newRole)) {
            user.setActiveRole(newRole);
        } else {
            throw new IllegalArgumentException("User does not have the role: " + newRole);
        }
    }
}
