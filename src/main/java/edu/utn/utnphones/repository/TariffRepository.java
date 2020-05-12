package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    public Optional<Tariff> findById(Integer id);

}
