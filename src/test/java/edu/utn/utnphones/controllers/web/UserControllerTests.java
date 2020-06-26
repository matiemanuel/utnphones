package edu.utn.utnphones.controllers.web;

import edu.utn.utnphones.service.UserService;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class UserControllerTests {

    private UserService userService;

    @Before
    public void init() {
        userService = mock(UserService.class);

    }
}
