package edu.utn.utnphones.repository;

import edu.utn.utnphones.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<Call, Integer> {

    @Query(value = "Select * from calls where origin_number = ?1", nativeQuery = true)
    public List<Call> findbyOriginNumber(String number);

    public Optional<Call> findById(Integer id);

    @Query(value = "select * from calls  where origin_number= :origin_number and " +
            "call_date between :from and :to", nativeQuery = true)
    List<Call> getByOriginNumberAndDates(@Param("origin_number") String origin_number,@Param("from") Date from,
                                         @Param("to") Date to);
}
