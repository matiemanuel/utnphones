package edu.utn.utnphones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Call")
@Table(name = "calls")
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_call")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_invoice")
    private Invoice invoice;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "origin_number")
    private String origin_number;

    @Column(name = "destiny_number")
    private String destiny_number;

    @Column(name = "tariff_price")
    private Double tariff_price;

    @Column(name = "call_date")
    private String date;

}
