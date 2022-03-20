package com.example.demo_jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleToUserForm {
    String username;
    String roleName;
}
