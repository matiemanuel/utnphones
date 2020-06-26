package edu.utn.utnphones.projections;

public interface MostCalledProjection {

    public String name = null;
    public String lastname = null;
    public String mostCalled = null;

    public String getName();
    public String setName(String name);
    public String getLastname();
    public String setLastname(String lastName);
    public String getMostCalled();
    public String setMostCalled(String mostCalled);

}
