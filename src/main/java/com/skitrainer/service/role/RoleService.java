package com.skitrainer.service.role;

import com.skitrainer.model.Role;
import com.skitrainer.model.User;

public interface RoleService {
    void switchRole(User user, Role newRole);
}
