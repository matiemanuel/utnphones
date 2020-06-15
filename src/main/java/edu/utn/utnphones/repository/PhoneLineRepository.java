package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.PhoneLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneLineRepository extends JpaRepository<PhoneLine, Integer> {

    @Query(value = "Select * from users where line_number = ?1", nativeQuery = true)
    public List<PhoneLine> findbyLineNumber(String linenumber);

    public Optional<PhoneLine> findById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update phone_lines set status = ?1 where id_phone_line = ?2", nativeQuery = true)
    public void updateStatus(String status, Integer idPhoneLine);




}
