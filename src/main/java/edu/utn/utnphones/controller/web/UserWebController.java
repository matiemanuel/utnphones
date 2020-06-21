package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.service.InvoiceService;
import edu.utn.utnphones.service.UserService;
import edu.utn.utnphones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserWebController {
    private final CallService callService;
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final SessionManager sessionManager;


    @Autowired
    public UserWebController(CallService callService,InvoiceService invoiceService,UserService userService, SessionManager sessionManager) {
        this.callService = callService;
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/mostCalled")
    public MostCalledProjection getMostCalledFromUser (@RequestHeader("Authorization") String authorization){
        return userService.getMostCalledFromUser(sessionManager.getCurrentUser(authorization).getId());
    }

    @GetMapping("/getCallsByDates/")
    public ResponseEntity<List<Call>> getCallsByDates(@RequestHeader("Authorization") String sessionToken, @RequestParam String from,
                                                      @RequestParam String to) throws ParseException {
        List<Call> calls = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if ((from != null) && (to != null)){
            Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
            calls = callService.getCallsByDates(currentUser.getId(), fromDate, toDate);
        }
        return (calls.size() > 0) ?  ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getInvoicesByDates/")
    public ResponseEntity<List<Invoice>> getInvoicesByDates(@RequestHeader("Authorization") String sessionToken, @RequestParam String from,
                                                            @RequestParam String to) throws ParseException {
        List<Invoice> invoices = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if ((from != null) && (to != null)){
            Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
            invoices = invoiceService.getInvoicesByDates(currentUser.getId(), fromDate, toDate);
        }
        return (invoices.size() > 0) ?  ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
