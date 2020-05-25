package edu.utn.utnphones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Invoice")
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice")
    private Integer id;

    @Column(name = "number_of_calls")
    private Integer number_of_calls;

    @Column(name = "price_cost")
    private Double price_cost;

    @Column(name = "total_price")
    private Double total_price;

    @Column(name = "invoice_date")
    private Date invoice_date;

    @Column(name = "expiration_date")
    private Date expiration_date;

    @Column(name = "paid")
    private Boolean paid;

    @ManyToOne(targetEntity = PhoneLine.class, fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "fk_id_phone_line", referencedColumnName = "id_phone_line")
    private PhoneLine phoneline;


    @OneToMany(mappedBy = "invoice")
    private List<Call> calls;
}
