package edu.utn.utnphones.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "City")
@Table(name = "Cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Integer id;

    private String name;

    private String prefix;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_province", referencedColumnName = "id_province")
    private Province province;

}