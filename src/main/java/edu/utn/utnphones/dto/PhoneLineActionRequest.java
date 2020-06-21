package edu.utn.utnphones.dto;

import edu.utn.utnphones.model.PhoneLine.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneLineActionRequest {

    private Integer phoneLineId;
    private Status status;

}
