package com.example.demo_jwt.service;

import com.example.demo_jwt.dto.RoleDTO;
import com.example.demo_jwt.dto.UserDTO;
import com.example.demo_jwt.models.Role;
import com.example.demo_jwt.models.User;
import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO user);
    RoleDTO saveRole(RoleDTO role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<UserDTO> getUsers();
}
