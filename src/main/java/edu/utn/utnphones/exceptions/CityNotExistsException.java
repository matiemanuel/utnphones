package edu.utn.utnphones.exceptions;

public class CityNotExistsException extends Exception {
    public String getMessage(){
        return "No existe ciudad.";
    }
}
