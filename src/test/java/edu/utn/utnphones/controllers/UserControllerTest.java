package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.service.UserService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    UserService userService;

    UserController controller;

    @Before
    public void setup() {
        userService = mock(UserService.class);
        controller = new UserController(userService);
    }

    @Test
    public void mostCalledTest() {
        MostCalledProjection projection = new MostCalledProjection() {
            @Override
            public String getName() {
                return "matias";
            }

            @Override
            public String getLastname() {
                return "cer";
            }

            @Override
            public String getMostCalled() {
                return "223";
            }
        };

        when(userService.getMostCalledFromUser(1)).thenReturn(projection);

        MostCalledProjection mostCalledFromUser = controller.getMostCalledFromUser(1);

        assertEquals("matias", mostCalledFromUser.getName());
        assertEquals("cer", mostCalledFromUser.getLastname());
        assertEquals("223", mostCalledFromUser.getMostCalled());


    }

}
