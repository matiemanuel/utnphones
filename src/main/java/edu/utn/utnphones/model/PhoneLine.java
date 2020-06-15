package edu.utn.utnphones.model;

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city")
    private City city;

    //ENUMS
    public enum Status {
        active, disabled, suspended;
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

    @OneToMany(mappedBy = "phoneline")
    private List<Invoice> invoices;

}
