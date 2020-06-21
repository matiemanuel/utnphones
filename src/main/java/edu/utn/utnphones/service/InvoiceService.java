package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.InvoiceNotExistsException;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.projections.InvoiceByDates;
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

    public Invoice findById(Integer id) throws InvoiceNotExistsException {
        return invoiceRepository.findById(id).orElseThrow(InvoiceNotExistsException::new);
    }

    public List<Invoice> getInvoicesByUser(Integer idUser) {
        return invoiceRepository.findByUserId(idUser);
    }

    public List<InvoiceByDates> getInvoicesByDates(Integer idUser, Date from, Date to) {
        return invoiceRepository.getInvoicesByDates(idUser, from, to);
    }
}
