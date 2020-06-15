package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.CallRequestDto;
import edu.utn.utnphones.exceptions.CallNotExistsException;
import edu.utn.utnphones.exceptions.PhoneLineNotExistsException;
import edu.utn.utnphones.exceptions.TariffNotExistsException;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.model.Tariff;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.*;
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
@RequestMapping("/backoffice/")
public class BackOfficeWebController {

    private final SessionManager sessionManager;
    private final CallService callService;
    private final UserService userService;
    private final PhoneLineService phoneLineService;
    private final TariffService tariffService;
    private final InvoiceService invoiceService;

// SOLO VAN A QUEDAR LAS CONTROLADORAS WEB, LAS DEMAS VAN A SER ELIMINADAS PARA EVITAR TESTEOS INNECESARIOS         
// TODO: 15/6/2020 MANEJO DE CLIENTES  UPDATE AND REMOVE

    @Autowired
    public BackOfficeWebController(UserService userService,CallService callService,PhoneLineService phoneLineService,
            TariffService tariffService,InvoiceService invoiceService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.callService = callService;
        this.tariffService = tariffService;
        this.phoneLineService = phoneLineService;
        this.invoiceService = invoiceService;
    }


    // USERS
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("addUser")
    public ResponseEntity newUser(@RequestHeader("Authorization") String sessionToken, @RequestBody User user ){
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String sessionToken) {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<User> users = userService.getAll(currentUser.getName());
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //CALLS
    @PostMapping("addCall")
    public ResponseEntity newCall(@RequestHeader("Authorization") String sessionToken,@RequestBody CallRequestDto callDto) throws CallNotExistsException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(callService.addCall(callDto.getOrigin(),
                callDto.getDestiny(), callDto.getDuration()));
    }

    @GetMapping("{userId}/calls")
    public ResponseEntity<List<Call>> getCallsByUser(@RequestHeader("Authorization") String sessionToken, @PathVariable Integer idUser){
        List<Call> calls = new ArrayList<>();
        calls = callService.getCallsByUser(idUser);
        return (calls.size() > 0) ?  ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //TARIFFS
    @GetMapping("tariffs/{tariffId}")
    public Tariff getTariffbyId(@PathVariable Integer tariffId) throws TariffNotExistsException {
        return this.tariffService.findById(tariffId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("tariffs")
    public ResponseEntity<List<Tariff>> getTariffs(@RequestHeader("Authorization") String sessionToken) {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Tariff> tariffs = tariffService.getAll();
        return (tariffs.size() > 0) ? ResponseEntity.ok(tariffs) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    //PHONELINES
    @GetMapping("activePhoneLine/{phonelineId}/")
    public void activePhoneLine(@RequestHeader("Authorization") String sessionToken, @PathVariable Integer phonelineId) throws PhoneLineNotExistsException {
        phoneLineService.updateStatus("active", phonelineId);
    }

    @GetMapping("/disabledPhoneLine/{phonelineId}")
    public void disabledPhoneLine(@RequestHeader("Authorization") String sessionToken, @PathVariable Integer phonelineId) throws PhoneLineNotExistsException {
        phoneLineService.updateStatus("disabled", phonelineId);
    }
    @GetMapping("suspendedPhoneLine/{phonelineId}/")
    public void suspendedPhoneLine(@RequestHeader("Authorization") String sessionToken, @PathVariable Integer phonelineId) throws PhoneLineNotExistsException {
        phoneLineService.updateStatus("suspended", phonelineId);
    }

    //INVOICES
    @GetMapping("/invoices/{userId}/dates/")
    public ResponseEntity<List<Invoice>> getInvoicesByDatesAndByUser(@RequestHeader("Authorization") String sessionToken, @RequestParam String from,
                                                            @RequestParam String to, @PathVariable Integer userId) throws ParseException, UserNotExistsException {
        List<Invoice> invoices = new ArrayList<>();
        User user= userService.findById(userId);
        if ((from != null) && (to != null) && (user!=null)){
            Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
            invoices = invoiceService.getInvoicesByDates(userId, fromDate, toDate);
        }
        return (invoices.size() > 0) ?  ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/invoices/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUser(@RequestHeader("Authorization") String sessionToken, @PathVariable Integer userId) throws UserNotExistsException {
        List<Invoice> invoices = new ArrayList<>();
        User user= userService.findById(userId);
        if (user != null){
            invoices = invoiceService.getInvoicesByUser(userId);
        }
        return (invoices.size() > 0) ?  ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
