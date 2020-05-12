package edu.utn.utnphones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "City")
@Table(name = "Cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String prefix;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Province province;

    @OneToMany(mappedBy = "City")
    private List<Tariff> tariffs;

}