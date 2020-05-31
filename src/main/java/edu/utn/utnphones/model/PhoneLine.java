package edu.utn.utnphones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "PhoneLine")
@Table(name = "phone_lines")
public class PhoneLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phone_line")
    private Integer id;

    @Column(name = "line_number")
    private String line_number;


    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JsonBackReference // este objeto user en realidad es una referecia.
    // al momento de printear una persona nos muestra adentro la linea de telefono y nos evita volver a mostrar la persona
    // y evitar un bucle infinito.
    @JoinColumn(name = "fk_id_user", referencedColumnName = "id_user")
    private User user;


    @ManyToOne(targetEntity = City.class, fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "fk_id_city", referencedColumnName = "id_city")
    private City city;

    //ENUMS
    public enum Status {
        active, disabled;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status phone_line_status;

    public enum Type {
        mobile, residential;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type")
    private Type phone_line_type;

    // lista de facturas
    @OneToMany(mappedBy = "phoneline")
    private List<Invoice> invoices;


}
