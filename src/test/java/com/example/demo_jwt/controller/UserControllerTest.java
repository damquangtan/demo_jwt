package com.example.demo_jwt.controller;

import com.example.demo_jwt.dto.RoleDTO;
import com.example.demo_jwt.dto.RoleToUserForm;
import com.example.demo_jwt.dto.UserDTO;
import com.example.demo_jwt.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUsers_userServiceReturnListUser_returnStatusOK() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        List<UserDTO> users = Arrays.asList(
                new UserDTO(1L, "name1", "username1", "password1", new ArrayList<>()),
                new UserDTO(2L, "name2", "username2", "password2", new ArrayList<>()),
                new UserDTO(3L, "name3", "username3", "password3", new ArrayList<>())
        );

        when(userService.getUsers()).thenReturn(users);

        // when
        ResponseEntity<?> response = userController.getUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getUsers_userServiceReturnListUser_returnListUserInBodyOfResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        List<UserDTO> users = Arrays.asList(
                new UserDTO(1L, "name1", "username1", "password1", new ArrayList<>()),
                new UserDTO(2L, "name2", "username2", "password2", new ArrayList<>()),
                new UserDTO(3L, "name3", "username3", "password3", new ArrayList<>())
        );

        when(userService.getUsers()).thenReturn(users);

        // when
        ResponseEntity<?> response = userController.getUsers();

        // then
        List<UserDTO> actualResult = (List<UserDTO>) response.getBody();
        assertEquals(3, actualResult.size());
        for (int i = 0; i < actualResult.size(); i++) {
            String expectName = "name" + (i + 1);
            String expectUsername = "username" + (i + 1);
            String expectPassword = "password" + (i + 1);

            assertEquals(expectName, actualResult.get(i).getName());
            assertEquals(expectUsername, actualResult.get(i).getUsername());
            assertEquals(expectPassword, actualResult.get(i).getPassword());
        }
    }

    @Test
    void saveUser_userServiceSaveUserSuccessful_returnUserInBodyOfResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        UserDTO userDTO = new UserDTO(1L, "name", "username", "password", new ArrayList<>());
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);

        // when
        ResponseEntity<UserDTO> response = userController.saveUser(userDTO);
        UserDTO responseBody = response.getBody();
        assertEquals(1L, responseBody.getId());
        assertEquals("name", responseBody.getName());
        assertEquals("username", responseBody.getUsername());
        assertEquals("password", responseBody.getPassword());
        assertThat(responseBody.getRoles()).isEmpty();
    }

    @Test
    void saveUser_userServiceSaveUserSuccessful_returnCreatedHttpStatus() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        UserDTO userDTO = new UserDTO(1L, "name", "username", "password", new ArrayList<>());
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);

        // when
        ResponseEntity<UserDTO> response = userController.saveUser(userDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void saveRole_userServiceSaveRoleSuccessful_returnCreatedHttpStatus() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        RoleDTO roleDTO = new RoleDTO(1L, "ROLE_USER");
        when(userService.saveRole(any(RoleDTO.class))).thenReturn(roleDTO);

        // when
        ResponseEntity<RoleDTO> response = userController.saveRole(roleDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void saveRole_userServiceSaveUserSuccessful_returnUserInBodyOfResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        RoleDTO roleDTO = new RoleDTO(1L, "ROLE_USER");
        when(userService.saveRole(any(RoleDTO.class))).thenReturn(roleDTO);

        // when
        ResponseEntity<RoleDTO> response = userController.saveRole(roleDTO);
        RoleDTO actualRoleResponse = response.getBody();

        // then
        assertEquals(roleDTO.getId(), actualRoleResponse.getId());
        assertEquals(roleDTO.getName(), actualRoleResponse.getName());
    }

    @Test
    void addRoleToUser_userServiceAddRoleToUserSuccessful_returnHttpStatusOK() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // given
        RoleToUserForm roleToUserForm = new RoleToUserForm("username", "ROLE_USER");
        // return void -> doNothing
        doNothing().when(userService).addRoleToUser(any(String.class), any(String.class));
        userService.addRoleToUser(roleToUserForm.getUsername(), roleToUserForm.getRoleName());

        // when
        ResponseEntity response = userController.addRoleToUser(roleToUserForm);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // TODO: can define lai JWTUtility sau do moi test
    @Test
    void refreshToken() {
    }
}