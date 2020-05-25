package edu.utn.utnphones.exceptions;

public class UserNotExistsException extends Exception {
    public String getMessage(){
        return "No existe usuario.";
    }
}
