package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.City;
import edu.utn.utnphones.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Query(value = "Select * from City where name = ?1", nativeQuery = true)
    List<City> findbyName(String name);

}
