package edu.utn.utnphones;

import edu.utn.utnphones.controller.CityController;
import edu.utn.utnphones.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class UtnphonesApplicationTests {

	@Autowired
	CityController controller;


	@Test
	void contextLoads() {
	}

	@Test
	public void Testing() {
		ResponseEntity<List<City>> all = controller.getAll(null);
		assertEquals(HttpStatus.OK, all.getStatusCode());
		assertNotEquals(0, all.getBody().size());
	}

}