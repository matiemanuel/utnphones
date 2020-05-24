package edu.utn.utnphones.exceptions;

public class InvoiceNotExistsException extends Exception {
    public String getMessage(){
        return "No existe factura.";
    }
}
