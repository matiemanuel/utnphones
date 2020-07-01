package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.controller.web.LoginController;
import edu.utn.utnphones.dto.LoginRequestDto;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.exceptions.ValidationException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.session.SessionManager;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static edu.utn.utnphones.model.User.Status.active;
import static edu.utn.utnphones.model.User.Type.client;
import static edu.utn.utnphones.model.User.Type.employee;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTests {

    private LoginController loginControlller;

    private UserController userController;

    private SessionManager sessionManager;

    User correctUser = new User(1, "test", "testing", "123", "test@gmail.com", "test", null, employee, active);
    User userNotExist = new User(99, "invalid", "invalid", "XASs", "invalid@mail.com", "invalid", null, client, active);
    String token = "correctToken";


    @SneakyThrows
    @Before
    public void init() {
        userController = mock(UserController.class);
        sessionManager = mock(SessionManager.class);

        loginControlller = new LoginController(userController, sessionManager);
        when(userController.login(userNotExist.getEmail(), userNotExist.getPassword())).thenThrow(RecordNotExistsException.class);
        when(userController.login(correctUser.getEmail(), userNotExist.getPassword())).thenThrow(ValidationException.class);
        when(userController.login(correctUser.getEmail(), correctUser.getPassword())).thenReturn(correctUser);
    }

    @Test
    public void loginOkTest() throws ValidationException, RecordNotExistsException {
        LoginRequestDto request = new LoginRequestDto(correctUser.getEmail(), correctUser.getPassword());
        ResponseEntity response = loginControlller.login(request);
        assertEquals(SC_OK, response.getStatusCode().value());
    }

    @Test
    public void logoutTest(){
        ResponseEntity response = loginControlller.logout(token);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

}
