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
    private String lineNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city")
    private City city;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status phoneLineStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type")
    private Type phoneLineType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_invoice")
    private Invoice invoice;

    public enum Type {
        mobile, residential;
    }

    public enum Status {
        active, disabled, suspended;
    }

}
