package edu.utn.utnphones;

import edu.utn.utnphones.controller.CityController;
import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.model.City;
import edu.utn.utnphones.model.Province;
import edu.utn.utnphones.projections.MostCalledProjection;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UtnphonesApplicationTests {

    @Mock
    CityController controller;

    @Autowired
    UserController user;

    @Test
    void contextLoads() {
    }

    @Test
    public void Testing() {

        List<City> list = new ArrayList<>();
        list.add(new City(1, "Mar del Plata", "223", new Province(1, "Buenos Aires")));
        when(controller.getAll(null)).thenReturn(ResponseEntity.ok(list));

        ResponseEntity<List<City>> all = controller.getAll(null);

        assertEquals(HttpStatus.OK, all.getStatusCode());
        assertNotEquals(0, all.getBody().size());
    }

}
