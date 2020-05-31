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
@Table(name = "tariffs")
public class Tariff {

    @Column(name = "id_tariff")
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_origin_city")
    private City origin_city;

    @ManyToOne
    @JoinColumn(name = "id_destiny_city")
    private City destiny_city;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "price")
    private Double price;

}
