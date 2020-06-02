package edu.utn.utnphones.controller;


import edu.utn.utnphones.exceptions.CityNotExistsException;
import edu.utn.utnphones.model.City;
import edu.utn.utnphones.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public City getCitybyId(@PathVariable Integer cityId) throws CityNotExistsException {// parametro de la url
        return this.cityService.findById(cityId);
    }

    @PostMapping("/")
    public void addCity(@RequestBody City newCity) {// transforma el json en una entidad
        cityService.addCity(newCity);
    }

    @GetMapping("/")
    public ResponseEntity<List<City>> getAll(@RequestParam(required = false) String name) {
        List<City> list = this.cityService.getAll(name);
        if (list.size()==0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

}
