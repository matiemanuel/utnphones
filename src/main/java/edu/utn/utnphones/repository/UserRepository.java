package edu.utn.utnphones.repository;


import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "Select * from users where name = ?1", nativeQuery = true)
    public List<User> findbyName(String name);

    User getByEmailAndPassword(String email, String password);

    public Optional<User> findById(Integer id);

    @Query(value = "call sp_mostDestination (?1 , ?2);", nativeQuery = true)
    public List<MostCalledProjection> getMostCalledFromUser(Integer userId, Integer size);

    @Query(value = "Select * from users where email = ?1", nativeQuery = true)
    public User findByEmail(String Email);

    @Query(value = "UPDATE users " +
            "SET name = ?1, lastname = ?2, password = ?3, id_city = ?4, type = ?5, status = ?6 " +
            "WHERE email = ?7", nativeQuery = true)
    public User updateUser(String name, String lastName, String password, Integer id_city, String type,
                           String status, String email);
}
