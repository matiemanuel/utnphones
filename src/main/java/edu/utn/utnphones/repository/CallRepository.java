package edu.utn.utnphones.repository;

import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.projections.CallsByDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<Call, Integer> {

    @Query(value = "Select * from calls where origin_number = ?1", nativeQuery = true)
    public List<Call> findbyOriginNumber(String number);

    public Optional<Call> findById(Integer id);

    @Query(value = "call sp_add_call(?1, ?2, ?3)", nativeQuery = true)
    public Integer addCall(String origin_number, String destiny_number, Integer duration);

    @Query(value = "call sp_callsByUserAndDates(?1, ?2, ?3)", nativeQuery = true)
    List<CallsByDates> getCallsByDates(@Param("id_user") Integer idUser, @Param("from") Date from,
                                       @Param("to") Date to);

    @Query(value = "select * from calls c " +
            "join phone_lines pl\n" +
            "\ton c.origin_number = pl.line_number\n" +
            "where pl.id_user= :id_user", nativeQuery = true)
    List<Call> getCallsByUser(@Param("id_user") Integer idUser);
}
