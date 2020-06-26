package edu.utn.utnphones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallRequestDto {

    String origin;
    String destiny;
    Integer duration;

}
