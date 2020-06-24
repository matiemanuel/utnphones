package edu.utn.utnphones.model;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private Type user_type;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status user_status;

    public enum Type {
        client, employee;
    }

    public enum Status {
        active, disabled;
    }

}
