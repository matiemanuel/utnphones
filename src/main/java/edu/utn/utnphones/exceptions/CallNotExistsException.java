package edu.utn.utnphones.exceptions;

public class CallNotExistsException extends Exception {
    public String getMessage(){
        return "No existe llamada.";
    }
}
