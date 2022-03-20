package com.example.demo_jwt.dto;

import com.example.demo_jwt.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Collection<RoleDTO> roles = new ArrayList<>();
}
