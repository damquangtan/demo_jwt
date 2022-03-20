package com.example.demo_jwt.models;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void getId_setIdIsOne_getIdMethodReturnValueOne() {
        User user = new User(null, "name", "username", "password", new ArrayList<>());
        user.setId(1L);

        Long actualValueOfId = user.getId();
        assertEquals(1L, actualValueOfId);
    }

    @Test
    void getName_setNameHasValueIsTest_getNameMethodReturnTestValue() {
        User user = new User();
        user.setName("Test");

        String actualValueOfName = user.getName();
        assertEquals("Test", actualValueOfName);
    }

    @Test
    void getUsername_setUsernameHasValueIsDemo_getUsernameMethodReturnDemoValue() {
        // prepare data
        User user = new User();
        user.setUsername("Demo");

        String actualUsernameValue = user.getUsername();
        assertEquals("Demo", actualUsernameValue, "Same value of expected and actual result");
    }

    @Test
    void getPassword_setPasswordHasValueIsPassword_getPasswordMethodReturnPasswordValue() {
        // prepare
        User user = new User();
        user.setPassword("Password");

        String actualPasswordValue = user.getPassword();
        assertEquals("Password", actualPasswordValue);
    }

    @Test
    void getRoles_setRoleValue_getRoleReturnSameValueAssetRole() {
        // prepare data
        User user = new User();
        List<Role> roles = Arrays.asList(
                new Role(1L, "firstValue"),
                new Role(2L, "secondValue")
        );
        user.setRoles(roles);

        List<Role> actualRolesValue = new ArrayList<>(user.getRoles());
        int sizeOfRole = actualRolesValue.size();
        assertEquals(2, sizeOfRole);

        Role firstActualRole = actualRolesValue.get(0);
        assertEquals(1L, firstActualRole.getId());
        assertEquals("firstValue", firstActualRole.getName());

        Role secondActualRole = actualRolesValue.get(1);
        assertEquals(2L, secondActualRole.getId());
        assertEquals("secondValue", secondActualRole.getName());
    }

    @Test
    void setId_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setId(1L);

        // then
        final Field field = user.getClass().getDeclaredField("id");
        field.setAccessible(true);
        assertNotEquals(2L, field.get(user));
    }

    @Test
    void setId_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setId(1L);

        // then
        final Field field = user.getClass().getDeclaredField("id");
        field.setAccessible(true);
        assertEquals(1L, field.get(user));
    }

    @Test
    void setName_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setName("name");

        // then
        final Field field = user.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertNotEquals("demo", field.get(user));
    }

    @Test
    void setName_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setName("name");

        // then
        final Field field = user.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("name", field.get(user));
    }

    @Test
    void setUsername_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setUsername("username");

        // then
        final Field field = user.getClass().getDeclaredField("username");
        field.setAccessible(true);
        assertNotEquals("demo", field.get(user));
    }

    @Test
    void setUsername_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setUsername("username");

        // then
        final Field field = user.getClass().getDeclaredField("username");
        field.setAccessible(true);
        assertEquals("username", field.get(user));
    }

    @Test
    void setPassword_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setPassword("password");

        // then
        final Field field = user.getClass().getDeclaredField("password");
        field.setAccessible(true);
        assertNotEquals("test", field.get(user));
    }

    @Test
    void setPassword_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();

        // when
        user.setPassword("password");

        // then
        final Field field = user.getClass().getDeclaredField("password");
        field.setAccessible(true);
        assertEquals("password", field.get(user));
    }

    @Test
    void setRoles_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final User user = new User();
        List<Role> roles = Arrays.asList(
                new Role(1L, "firstValue"),
                new Role(2L, "secondValue")
        );

        // when
        user.setRoles(roles);

        // then
        final Field field = user.getClass().getDeclaredField("roles");
        field.setAccessible(true);
        Object actualRolesObjectValue = field.get(user);
        List<Role> actualRolesListValue = (List<Role>) actualRolesObjectValue;

        assertEquals(2, actualRolesListValue.size());

        Role firstValue = actualRolesListValue.get(0);
        assertEquals(1L, firstValue.getId());
        assertEquals("firstValue", firstValue.getName());

        Role secondValue = actualRolesListValue.get(1);
        assertEquals(2L, secondValue.getId());
        assertEquals("secondValue", secondValue.getName());
    }
}