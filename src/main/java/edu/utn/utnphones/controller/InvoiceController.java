package edu.utn.utnphones.controller;

import edu.utn.utnphones.exceptions.InvoiceNotExistsException;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoicebyId(@PathVariable Integer invoiceId) throws InvoiceNotExistsException {
        return this.invoiceService.findById(invoiceId);
    }

    @PostMapping("/")
    public void addInvoice(@RequestBody Invoice newInvoice) {
        invoiceService.addInvoice(newInvoice);
    }

    @GetMapping("/")
    public List<Invoice> getAll() {
        return invoiceService.getAll();
    }

}
