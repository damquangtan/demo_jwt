package com.example.demo_jwt.service;

import com.example.demo_jwt.dto.RoleDTO;
import com.example.demo_jwt.dto.UserDTO;
import com.example.demo_jwt.exceptions.RoleNotFoundException;
import com.example.demo_jwt.models.Role;
import com.example.demo_jwt.models.User;
import com.example.demo_jwt.repo.RoleRepo;
import com.example.demo_jwt.repo.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void loadUserByUsername_userIsNull_throwUsernameNotFoundException() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.loadUserByUsername("user"));
    }

    @Test
    void loadUserByUsername_findUserByUsernameReturnValue_ReturnUserInfo() {
        // given
        List<Role> roles = Arrays.asList(
                new Role(1L, "ROLE_USER"),
                new Role(2L, "ROLE_ADMIN")
        );
        User user = new User(1L, "name", "username", "password", roles);
        when(userRepo.findByUsername(any(String.class))).thenReturn(user);

        // when
        UserDetails result = userServiceImpl.loadUserByUsername("user");

        // then
        assertInstanceOf(UserDetails.class, result);
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(2, result.getAuthorities().size());

        List<String> actualRoles = result.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        assertTrue(actualRoles.contains("ROLE_USER"));
        assertTrue(actualRoles.contains("ROLE_ADMIN"));
    }

    @Test
    void saveUser_userRepoSaveUserSuccessful_userInfoReturnedSameInfoOfUserInput() {
        // given
        List<Role> roles = Arrays.asList(
                new Role(1L, "ROLE_USER"),
                new Role(2L, "ROLE_ADMIN")
        );

        List<RoleDTO> rolesDTO = Arrays.asList(
                new RoleDTO(1L, "ROLE_USER"),
                new RoleDTO(2L, "ROLE_ADMIN")
        );
        UserDTO userDTO = new UserDTO(1L, "name", "username", "password", rolesDTO);
        User user = new User(1L, "name", "username", "password", roles);

        doReturn(user).when(mapper).map(userDTO, User.class);

        String encodePassword = "123@#$%$dfdff2";
        when(passwordEncoder.encode(any(String.class))).thenReturn(encodePassword);
        when(userRepo.save(user)).thenReturn(user);

        doReturn(userDTO).when(mapper).map(user, UserDTO.class);
        // when
        UserDTO actualUserResult = userServiceImpl.saveUser(userDTO);
        // then
        assertNotNull(actualUserResult);
        assertEquals(user.getId(), actualUserResult.getId());
        assertEquals(user.getName(), actualUserResult.getName());
        assertEquals(user.getUsername(), actualUserResult.getUsername());
        assertEquals(encodePassword, actualUserResult.getPassword());
        assertEquals(2, actualUserResult.getRoles().size());

        List<String> actualRoles = actualUserResult.getRoles().stream()
                .map(RoleDTO::getName).collect(Collectors.toList());

        assertTrue(actualRoles.contains("ROLE_USER"));
        assertTrue(actualRoles.contains("ROLE_ADMIN"));
    }

    @Test
    void saveRole_roleRepoSaveRoleSuccessful_roleInfoReturnedSameInfoOfRoleInput() {
        // given
        RoleDTO roleInput = new RoleDTO(1L, "ROLE_TEST");
        Role role = new Role(1L, "ROLE_TEST");
        doReturn(role).when(mapper).map(roleInput, Role.class);
        when(roleRepo.save(any(Role.class))).thenReturn(role);
        doReturn(roleInput).when(mapper).map(role, RoleDTO.class);

        // when
        RoleDTO actualRoleResult = userServiceImpl.saveRole(roleInput);
        // then
        assertNotNull(actualRoleResult);
        assertEquals(roleInput.getId(), actualRoleResult.getId());
        assertEquals(roleInput.getName(), actualRoleResult.getName());
    }

    @Test
    void addRoleToUser_userNotFoundInDatabase_throwUsernameNotFoundException() {
        // given
        User user = new User(1L, "name", "username", "password", new ArrayList<>());
        Role role = new Role(1L, "ROLE_TEST");
        when(userRepo.findByUsername(any(String.class))).thenReturn(null);

        // then
        assertThrows(UsernameNotFoundException.class,
                () -> userServiceImpl.addRoleToUser(user.getUsername(), role.getName()));
    }

    @Test
    void addRoleToUser_userAndRoleFoundInDatabase_throwRoleNotFoundException() {
        // given
        User user = new User(1L, "name", "username", "password", new ArrayList<>());
        Role role = new Role(1L, "ROLE_TEST");
        when(userRepo.findByUsername(any(String.class))).thenReturn(user);
        when(roleRepo.findByName(any(String.class))).thenReturn(null);

        // then
        assertThrows(RoleNotFoundException.class,
                () -> userServiceImpl.addRoleToUser(user.getUsername(), role.getName()));
    }

    @Disabled
    @Test
    void addRoleToUser_userAndRoleFoundInDatabase_collectionRoleInUserContainRoleAdded() {
        // given
        User user = new User(1L, "name", "username", "password", new ArrayList<>());
        Role roleToAdd = new Role(1L, "ROLE_TEST");
        when(userRepo.findByUsername(any(String.class))).thenReturn(user);
        when(roleRepo.findByName(any(String.class))).thenReturn(roleToAdd);

        // when
        doNothing().when(userServiceImpl).addRoleToUser(user.getUsername(), roleToAdd.getName());
        // then
        // TODO: How can test when using casecade type all of hibernate
    }

    @Test
    void getUser_findByUsernameMethodReturnNull_returnNull() {
        // given
        String username = "user";
        when(userRepo.findByUsername(any(String.class))).thenReturn(null);
        // when
        User user = userServiceImpl.getUser(username);
        // then
        assertNull(user);
    }

    @Test
    void getUser_findByUsernameMethodReturnValue_returnUser() {
        // given
        String username = "user";
        User user = new User(1L, "name", "username", "password", new ArrayList<>());
        when(userRepo.findByUsername(any(String.class))).thenReturn(user);
        // when
        User actualResult = userServiceImpl.getUser(username);
        // then
        assertNotNull(actualResult);
        assertEquals(user.getId(), actualResult.getId());
        assertEquals(user.getUsername(), actualResult.getUsername());
        assertEquals(user.getName(), actualResult.getName());
        assertEquals(user.getPassword(), actualResult.getPassword());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void getUsers_findAllReturnEmptyList_returnEmptyList() {
        // given
        when(userRepo.findAll()).thenReturn(new ArrayList<>());
        // when
        List<UserDTO> actualResult = userServiceImpl.getUsers();
        // then
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void getUsers_findAllReturnValue_returnListUsers() {
        // given
        List<User> users = Arrays.asList(
                new User(1L, "name1", "username1", "password1", new ArrayList<>()),
                new User(2L, "name2", "username2", "password2", new ArrayList<>()),
                new User(3L, "name3", "username3", "password3", new ArrayList<>())
        );
        when(userRepo.findAll()).thenReturn(users);
        UserDTO userExpect = new UserDTO(4L, "name4", "username4", "password4", new ArrayList<>());
        users.forEach(user -> {
            doReturn(userExpect).when(mapper).map(user, UserDTO.class);
        });
        // when
        List<UserDTO> actualResult = userServiceImpl.getUsers();
        // then
        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());

        actualResult.forEach(result -> {
            assertEquals(userExpect.getId(), result.getId());
            assertEquals(userExpect.getName(), result.getName());
            assertEquals(userExpect.getPassword(), result.getPassword());
            assertEquals(userExpect.getUsername(), result.getUsername());
            assertTrue(result.getRoles().isEmpty());
        });
    }
}