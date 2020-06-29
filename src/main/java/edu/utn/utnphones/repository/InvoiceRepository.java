package edu.utn.utnphones.repository;

import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.projections.InvoiceByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query(value = "call sp_invoicesByUser(?1)", nativeQuery = true)
    List<InvoiceByUser> getInvoicesByUser(@Param("id_user") Integer idUser);

    @Query(value = "call sp_invoicesByUserAndDates(?1, ?2, ?3)", nativeQuery = true)
    List<InvoiceByUser> getInvoicesByDates(@Param("id_user") Integer idUser, @Param("from") Date from,
                                           @Param("to") Date to);
}
