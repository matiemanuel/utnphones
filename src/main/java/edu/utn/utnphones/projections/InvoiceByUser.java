package edu.utn.utnphones.projections;

import edu.utn.utnphones.model.PhoneLine.Type;

public interface InvoiceByUser {

    public String getInvoiceId();
    public Integer getNumberOfCalls();
    public String getLineNumber();
    public Type getLineType();
    public Double getTotalPrice();
    public boolean getPaid();
    public String getDate();
    public String getExpirationDate();

}
