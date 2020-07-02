package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.exceptions.ValidationException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.UserService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    private UserService userService;

    private UserController userController;

    private User correctUser = new User(1, "test", "testLastname", "dni", "email",
            "password", null, null, null);

    private List<User> users = new ArrayList<>();

    @SneakyThrows
    @Before
    public void init() {
        users.add(correctUser);
        userService = mock(UserService.class);
        when(userService.login("validMail","validPass")).thenReturn(correctUser);
        when(userService.findById(1)).thenReturn(correctUser);
        when(userService.addUser(correctUser)).thenReturn(correctUser);
        when(userService.getAll()).thenReturn(users);
        userController = new UserController(userService);
    }

    @Test(expected = ValidationException.class)
    public void loginTests() throws RecordNotExistsException, ValidationException {
        User login = userController.login("validMail", "validPass");
        assertNotEquals(null, login);
        userController.login("validMail", null);
    }

    @Test
    public void getByIdTest() throws RecordNotExistsException {
        User userbyId = userController.getUserbyId(1);
        assertNotEquals(null, userbyId);
    }

    @Test
    public void createNewUser() throws InvalidRequestException, RecordAlreadyExistsException {
        User user = userController.addUser(correctUser);
        assertNotEquals(null, user);
    }

    @Test
    public void getAllUsers() {
        List<User> all = userController.getAll();
        assertEquals(1, all.size());
    }

    @Test
    public void loginValidationExceptions() throws RecordNotExistsException {
        try {
            userController.login(null, "validPass");
            assertTrue(false);
        }catch (ValidationException e) {
            assertTrue(true);
        }

        try {
            userController.login("validUsername", null);
            assertTrue(false);
        }catch (ValidationException e) {
            assertTrue(true);
        }
    }
}
