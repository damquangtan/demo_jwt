package com.example.demo_jwt.service;

import com.example.demo_jwt.constants.Constants;
import com.example.demo_jwt.dto.RoleDTO;
import com.example.demo_jwt.dto.UserDTO;
import com.example.demo_jwt.exceptions.RoleNotFoundException;
import com.example.demo_jwt.models.Role;
import com.example.demo_jwt.models.User;
import com.example.demo_jwt.repo.RoleRepo;
import com.example.demo_jwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            log.error(Constants.USER_NOT_FOUND_MSG);
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_MSG);
        }

        log.info("User found in the database: {}", username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Saving new user {} to the database", userDTO.getName());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // convert DTO to entity
        User user = mapper.map(userDTO, User.class);
        User userResult = userRepo.save(user);

        // convert entity to DTO and return value
        return mapper.map(userResult, UserDTO.class);
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Saving new role {} to the database", roleDTO.getName());

        // convert DTO to Entity
        Role role = mapper.map(roleDTO, Role.class);
        Role roleResult = roleRepo.save(role);

        // convert entity to DTO and return value
        return mapper.map(roleResult, RoleDTO.class);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", username, roleName);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_MSG);
        }

        Role role = roleRepo.findByName(roleName);
        if (role == null) {
            throw new RoleNotFoundException("Role not found in the database");
        }

        log.info("User {} found in the database", username);
        log.info("Role {} found in the database", roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<UserDTO> getUsers() {
        log.info("Fetching all users");
        List<User> users = userRepo.findAll();
        List<UserDTO> result = new ArrayList<>();
        if (!users.isEmpty()) {
            users.forEach(user -> {
                UserDTO userDTO = mapper.map(user, UserDTO.class);
                result.add(userDTO);
            });
        }
        return result;
    }
}
