package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.exceptions.NoCallsException;
import edu.utn.utnphones.projections.MostDurationProjection;
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
    public void mostDurationTest() throws NoCallsException {
        MostDurationProjection projection = new MostDurationProjection() {
            @Override
            public String getName() {
                return "franco";
            }

            @Override
            public String getLastName() {
                return "ferreyra";
            }

            @Override
            public String getDuration() {
                return "200";
            }

        };

        when(userService.getMostDurationFromUser()).thenReturn(projection);

        MostDurationProjection mostDurationFromUser = controller.getMostDurationFromUser();

        assertEquals("franco", mostDurationFromUser.getName());
        assertEquals("ferreyra", mostDurationFromUser.getLastName());
        assertEquals("200", mostDurationFromUser.getDuration());


    }

}