package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostDurationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "Select * from users where name = ?1", nativeQuery = true)
    public List<User> findbyName(String name);

    public Optional<User> findById(Integer id);


    @Query(value = "select u.name,u.lastname,c.duration from calls c\n" +
            "join phone_lines pl\n" +
            "\ton c.origin_number = pl.line_number\n" +
            "join users u\n" +
            "\ton pl.id_user = u.id_user\n" +
            "order by duration desc limit 1;", nativeQuery = true)
    public MostDurationProjection getMostDurationFromUser();
}
