package edu.utn.utnphones.projections;

import edu.utn.utnphones.model.User.Type;

public interface UsersByCityProjection {

    public Integer getUserId();

    public String getName();

    public String getLastname();

    public String getDni();

    public String getEmail();

    public String getPassword();

    public Integer getCityId();

    public Type getType();

}
