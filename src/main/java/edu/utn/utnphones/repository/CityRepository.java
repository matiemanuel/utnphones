package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Query(value = "Select * from city where name = ?1", nativeQuery = true)
    public List<City> findbyName(String name);

    public Optional<City> findById(Integer id);

}
