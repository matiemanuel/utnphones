package edu.utn.utnphones.controllers.web.backofficewebcontroller;

import edu.utn.utnphones.controller.web.BackOfficeWebController;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static edu.utn.utnphones.model.User.Status.active;
import static edu.utn.utnphones.model.User.Status.disabled;
import static edu.utn.utnphones.model.User.Type.client;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackOfficeUserServiceTests {

    private BackOfficeWebController controller;

    private UserService userService;

    private String token = "token";

    private User activeUser = new User(1, "Test", "TestLastName", "testdni", "test@mail.com",
                                                            "test", null, client, active);
    private User disabledUser = new User(2, "Test2", "TestLastName2", "testdni2", "test2@mail.com",
            "test2", null, client, disabled);

    private List<User> users = new ArrayList<>();

    @Before
    public void init() throws UserNotExistsException {
        userService = mock(UserService.class);
        users.add(activeUser);
        users.add(disabledUser);
        when(userService.addUser(activeUser)).thenReturn(activeUser);
        when(userService.findById(1)).thenReturn(activeUser);
        when(userService.findById(-1)).thenThrow(UserNotExistsException.class);
        when(userService.disableUser(1)).thenReturn(activeUser);
        when(userService.disableUser(-1)).thenThrow(UserNotExistsException.class);
        when(userService.getAll()).thenReturn(users);
        controller = new BackOfficeWebController(userService,null,null,null,null,null);
    }

    @Test
    public void newUserTest() {
        ResponseEntity responseEntity = controller.newUser(token, activeUser);
        assertEquals(SC_CREATED, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findById() throws UserNotExistsException {
        ResponseEntity response = controller.getById(token, 1);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test(expected = UserNotExistsException.class)
    public void findByInvalidId() throws UserNotExistsException {
        controller.getById(token, -1);
    }

    @Test
    public void disableUserWithValidId() throws UserNotExistsException {
        ResponseEntity response = controller.disableUser(token, 1);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test(expected = UserNotExistsException.class)
    public void disableUserWithInvalidId() throws UserNotExistsException {
        controller.disableUser(token, -1);
    }

    @Test
    public void getAllUsers() {
        ResponseEntity<List<User>> response = controller.getUsers(token);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test
    public void getAllUsersNoContent() {
        activeUser.setUser_status(disabled);
        ResponseEntity<List<User>> response = controller.getUsers(token);
        assertEquals(SC_NO_CONTENT, response.getStatusCodeValue());
    }

}
