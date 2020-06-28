package edu.utn.utnphones.model;


import com.sun.istack.NotNull;
import edu.utn.utnphones.dto.UpdateUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "lastname")
    private String lastname;

    @NotNull
    @Column(name = "dni", unique = true)
    private String dni;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city")
    private City city;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type")
    private Type userType;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status userStatus;

    public enum Type {
        client, employee;
    }

    public enum Status {
        active, disabled;
    }

    public void updateUser(UpdateUserDto update){
        if(!isNull(update.getName()))
            this.name = update.getName();
        if(!isNull(update.getLastname()))
            this.lastname = update.getLastname();
        if(!isNull(update.getDni()))
            this.dni = update.getDni();
        if(!isNull(update.getEmail()))
            this.email = update.getEmail();
        if(!isNull(update.getPassword()))
            this.password = update.getPassword();
        if(!isNull(update.getCity()))
            this.city = update.getCity();
        if(!isNull(update.getUserType()))
            this.userType = update.getUserType();
        if(!isNull(update.getUserStatus()))
            this.userStatus = update.getUserStatus();
    }
}
