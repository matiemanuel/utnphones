package edu.utn.utnphones.service;

import edu.utn.utnphones.model.City;
import edu.utn.utnphones.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void addCity(City newCity) {
        cityRepository.save(newCity);
    }

    public List<City> getAll(String name) {
        if (isNull(name)) {
            return cityRepository.findAll();// puede recibir un sort
        }
        return cityRepository.findbyName(name);// puede recibir un sort
    }

    public City findById(Integer id){
        Optional<City> optional = cityRepository.findById(id);
        return optional.get();
    }
}
