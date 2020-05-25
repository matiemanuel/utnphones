package edu.utn.utnphones.exceptions;

public class ProvinceNotExistsException extends Exception{
    public String getMessage(){
        return "No existe provincia.";
    }
}
