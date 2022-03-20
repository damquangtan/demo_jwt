package com.example.demo_jwt.models;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RoleTest {

    @Test
    void getId_setIdIsOne_getIdMethodReturnValueOne() {
        Role role = new Role(1L, "Test");
        Long id = role.getId();
        assertEquals(1L, id);
    }

    @Test
    void getName_setNameIsTest_returnValueOfNameIsTest() {
        Role role = new Role(1L, "Test");
        String name = role.getName();
        assertEquals("Test", name);
    }

    @Test
    void setId_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final Role role = new Role();

        // when
        role.setId(1L);

        // then
        final Field field = role.getClass().getDeclaredField("id");
        field.setAccessible(true);
        assertNotEquals(2L, field.get(role));
    }

    @Test
    void setId_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final Role role = new Role();

        // when
        role.setId(1L);

        // then
        final Field field = role.getClass().getDeclaredField("id");
        field.setAccessible(true);
        assertEquals(1L, field.get(role));
    }

    @Test
    void setName_setValueNotSameValueExpected_FieldDidNotMatch() throws NoSuchFieldException, IllegalAccessException {
        // given
        final Role role = new Role();

        // when
        role.setName("Test");

        // then
        final Field field = role.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertNotEquals("demo", field.get(role));
    }

    @Test
    void setName_setValueSameValueExpected_FieldMatched() throws NoSuchFieldException, IllegalAccessException {
        // given
        final Role role = new Role();

        // when
        role.setName("demo");

        // then
        final Field field = role.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("demo", field.get(role));
    }
}