package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.projections.InvoiceByUser;
import edu.utn.utnphones.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void addInvoice(Invoice newInvoice) {
        invoiceRepository.save(newInvoice);
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(Integer id) throws RecordNotExistsException {
        return invoiceRepository.findById(id).orElseThrow(() -> new RecordNotExistsException("Invoice id provided doesn't exist"));
    }

    public List<InvoiceByUser> getInvoicesByUser(Integer idUser) {
        return invoiceRepository.getInvoicesByUser(idUser);
    }

    public List<InvoiceByUser> getInvoicesByDates(Integer idUser, Date from, Date to) {
        return invoiceRepository.getInvoicesByDates(idUser, from, to);
    }
}
