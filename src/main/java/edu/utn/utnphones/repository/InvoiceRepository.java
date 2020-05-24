package edu.utn.utnphones.repository;

import edu.utn.utnphones.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    public Optional<Invoice> findById(Integer id);
}
