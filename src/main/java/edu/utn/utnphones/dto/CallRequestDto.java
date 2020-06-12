package edu.utn.utnphones.dto;

public class CallRequestDto {
    String origin;
    String destiny;
    Integer duration;

    public String getOrigin() {
        return origin;
    }

    public String getDestiny() { return destiny; }

    public Integer getDuration() {
        return duration;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
