package edu.utn.utnphones.exceptions;

public class TariffNotExistsException extends Exception {
    public String getMessage(){
        return "No existe tarifa.";
    }
}
