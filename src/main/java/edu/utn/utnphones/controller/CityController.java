package edu.utn.utnphones.controller;


import edu.utn.utnphones.model.City;
import edu.utn.utnphones.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/{cityId}")
    public City getCitybyId(@PathVariable Integer cityId) {// parametro de la url
        return new City();
    }

    @PostMapping("/")
    public void addCity(@RequestBody City newCity) {// transforma el json en una entidad
        cityService.addCity(newCity);
    }

    @GetMapping("/")
    public List<City> getAll(@RequestParam(required = false) String name) {
        return cityService.getAll(name);
    }

}
