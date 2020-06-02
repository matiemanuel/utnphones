package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.projections.UsersByCityProjection;
import edu.utn.utnphones.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @Test
    public void usersByCityTest() {
        List<UsersByCityProjection> results = new ArrayList<>();

        UsersByCityProjection projection = new UsersByCityProjection() {
            @Override
            public Integer getUserId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Matias";
            }

            @Override
            public String getLastname() {
                return "Cerano";
            }

            @Override
            public String getDni() {
                return "12345678";
            }

            @Override
            public String getEmail() {
                return "matiemanuel@gmail.com";
            }

            @Override
            public String getPassword() {
                return "1234abc";
            }

            @Override
            public Integer getCityId() {
                return 1;
            }

            @Override
            public User.Type getType() {
                return User.Type.client;
            }
        };

        results.add(projection);

        when(controller.getUsersByCity(1)).thenReturn(results);

        List<UsersByCityProjection> testResult = controller.getUsersByCity(1);

        assertNotEquals(0, testResult.size());

        UsersByCityProjection userTest = testResult.get(0);

        assertEquals("Matias", userTest.getName());
        assertEquals("Cerano", userTest.getLastname());
        assertEquals("12345678", userTest.getDni());
        assertEquals(1, userTest.getUserId());
        assertEquals(1, userTest.getCityId());
        assertEquals("matiemanuel@gmail.com", userTest.getEmail());
        assertEquals("1234abc", userTest.getPassword());
        assertEquals(User.Type.client, userTest.getType());

    }

}
