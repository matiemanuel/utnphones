package edu.utn.utnphones.exceptions;

public class PhoneLineNotExistsException extends Exception {
    public String getMessage(){
        return "No existe linea de telefono.";
    }
}
