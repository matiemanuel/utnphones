package edu.utn.utnphones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "City")
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Integer id;

    private String name;

    private String prefix;

    @ManyToOne(targetEntity = Province.class, fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "fk_id_province", referencedColumnName = "id_province")
    private Province province;

}