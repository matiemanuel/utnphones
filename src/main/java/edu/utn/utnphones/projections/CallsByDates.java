package edu.utn.utnphones.projections;

public interface CallsByDates {

    public Integer getCallId();
    public String getOriginNumber();
    public String getOriginCity();
    public String getDestinyNumber();
    public String getDestinyCity();
    public Integer getDuration();
    public String getTotalPrice();
    public String getDate();

    public void setCallId(Integer callId);
    public void setOriginNumber(String originNumber);
    public void setOriginCity(String originCity);
    public void setDestinyNumber(String destinyNumber);
    public void setDestinyCity(String destinyCity);
    public void setDuration(Integer duration);
    public void setTotalPrice(String totalPrice);
    public void setDate(String date);

}
