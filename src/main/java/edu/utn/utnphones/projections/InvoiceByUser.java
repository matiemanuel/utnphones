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

    public void setInvoiceId(String invoiceId);
    public void setNumberOfCalls(Integer numberOfCalls);
    public void setLineNumber(String lineNumber);
    public void setLineType(Type type);
    public void setTotalPrice(Double totalPrice);
    public void setPaid(Boolean paid);
    public void setDate(String date);
    public void setExpirationDate(String expirationDate);

}
