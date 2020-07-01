package edu.utn.utnphones.dto;

import edu.utn.utnphones.model.City;
import edu.utn.utnphones.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    private String name;

    private String lastname;

    private String password;

    private City city;

    private User.Type userType;

    private User.Status userStatus;

}

