package edu.utn.utnphones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Tariff")
@Table(name = "Tariffs")
public class Tariff {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private City origin_city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private City destiny_city;

    private Double cost;
    private Double price;

}
