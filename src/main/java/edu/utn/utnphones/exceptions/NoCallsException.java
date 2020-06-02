package edu.utn.utnphones.exceptions;

public class NoCallsException extends Exception {
    public String getMessage(){
        return "No hay llamadas en el sistema.";
    }
}
