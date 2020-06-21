package edu.utn.utnphones.repository;

import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.projections.InvoiceByDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    public Optional<Invoice> findById(Integer id);


    @Query(value = "select * from invoices i " +
            "join phone_lines pl\n" +
            "\ton i.id_phone_line = pl.id_phone_line\n" +
            "where pl.id_user= ?1 ", nativeQuery = true)
    public List<Invoice> findByUserId(Integer id);


    @Query(value = "call sp_invoicesByUserAndDates(?1, ?2, ?3)", nativeQuery = true)
    List<InvoiceByDates> getInvoicesByDates(@Param("id_user") Integer idUser, @Param("from") Date from,
                                            @Param("to") Date to);
}
