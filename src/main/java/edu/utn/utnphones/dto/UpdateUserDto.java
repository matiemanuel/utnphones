package edu.utn.utnphones.dto;

import com.sun.istack.NotNull;
import edu.utn.utnphones.model.City;
import edu.utn.utnphones.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    private String name;

    private String lastname;

    private String dni;

    private String email;

    private String password;

    private City city;

    private User.Type userType;

    private User.Status userStatus;

}

