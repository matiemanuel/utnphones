package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.PhoneLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneLineRepository extends JpaRepository<PhoneLine, Integer> {

    @Query(value = "Select * from users where line_number = ?1", nativeQuery = true)
    public List<PhoneLine> findbyLineNumber(String linenumber);

    public Optional<PhoneLine> findById(Integer id);

}
