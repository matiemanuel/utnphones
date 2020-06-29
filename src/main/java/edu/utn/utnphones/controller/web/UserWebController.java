package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.UpdateUserDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.projections.InvoiceByUser;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.service.InvoiceService;
import edu.utn.utnphones.service.UserService;
import edu.utn.utnphones.session.SessionManager;
import edu.utn.utnphones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
public class UserWebController {

    private final CallService callService;
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final SessionManager sessionManager;

    @Autowired
    public UserWebController(CallService callService, InvoiceService invoiceService, UserService userService, SessionManager sessionManager) {
        this.callService = callService;
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @ResponseStatus(OK)
    @PostMapping("/user")
    public ResponseEntity<URI> updateUser(@RequestHeader("Authorization") String sessionToken,
                                          @RequestBody UpdateUserDto updatedUser) throws RecordNotExistsException, RecordAlreadyExistsException {
        return ResponseEntity.status(OK).body(RestUtils.getLocation(userService.updateUser(sessionManager.getCurrentUser(sessionToken).getId(), updatedUser)));
    }

    @GetMapping("/calls")
    public ResponseEntity<List<Call>> getCallsByUser(@RequestHeader("Authorization") String sessionToken,
                                                     @RequestParam("userId") Integer userId) throws InvalidRequestException {
        List<Call> calls = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        calls = callService.getCallsByUser(sessionManager.getCurrentUser(sessionToken).getId());
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/callsByDates")
    public ResponseEntity<List<CallsByDates>> getCallsByDates(@RequestHeader("Authorization") String sessionToken,
                                                              @RequestParam("userId") Integer userId,
                                                              @RequestParam("from") String from,
                                                              @RequestParam("to") String to) throws InvalidRequestException {
        List<CallsByDates> calls = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        try {
            if(!isNull(from) && !isNull(to)){
                Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
                Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
                calls = callService.getCallsByDates(sessionManager.getCurrentUser(sessionToken).getId(), fromDate, toDate);
            }else
                throw new InvalidRequestException("Please provide service with dates 'from' and 'to'");
            return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ParseException ex) {
            throw new InvalidRequestException("Something went wrong when parsing dates, please provide dates with format: dd/MM/yyyy");
        }
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceByUser>> getInvoices(@RequestHeader("Authorization") String sessionToken) {
        List<InvoiceByUser> invoices = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        invoices = invoiceService.getInvoicesByUser(currentUser.getId());
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/invoicesByDates")
    public ResponseEntity<List<InvoiceByUser>> getInvoicesByDates(@RequestHeader("Authorization") String sessionToken,
                                                                  @RequestParam("from") String from,
                                                                  @RequestParam("to") String to) throws InvalidRequestException {
        List<InvoiceByUser> invoices = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        try {
            if ((from != null) && (to != null)) {
                Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
                Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
                invoices = invoiceService.getInvoicesByDates(currentUser.getId(), fromDate, toDate);
            } else {
                throw new InvalidRequestException("Please provide service with dates 'from' and 'to'");
            }
        } catch (ParseException ex) {
            throw new InvalidRequestException("Something went wrong when parsing dates, please provide dates with format: dd/MM/yyyy");
        }
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/mostCalled")
    public ResponseEntity<MostCalledProjection> getMostCalledFromUser(@RequestHeader("Authorization") String authorization) throws InvalidRequestException {
        MostCalledProjection mostCalledFromUser = userService.getMostCalledFromUser(sessionManager.getCurrentUser(authorization).getId());
        if (mostCalledFromUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (mostCalledFromUser.getMostCalled() == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mostCalledFromUser);
    }
}
