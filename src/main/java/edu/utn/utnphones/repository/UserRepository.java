package edu.utn.utnphones.repository;


import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.projections.UsersByCityProjection;
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

    @Query(value = "SELECT s.name, s.lastname, (Select destiny_number from calls c\n" +
            "join phone_lines pl\n" +
            "\ton c.origin_number = pl.line_number\n" +
            "group by c.destiny_number\n" +
            "order by count(c.destiny_number) desc limit 1) as mostCalled from users s\n" +
            "where s.id_user = ?1", nativeQuery = true)
    public MostCalledProjection getMostCalledFromUser(Integer userId);

    @Query(value = "SELECT name, lastname, dni, email, password, id_city as cityId, id_user as userId, type  FROM users " +
            "where id_city = ?1", nativeQuery = true)
    public List<UsersByCityProjection> getUsersByCity(Integer cityId);

}
