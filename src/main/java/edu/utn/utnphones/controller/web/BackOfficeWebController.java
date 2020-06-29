package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.PhoneLineActionRequest;
import edu.utn.utnphones.dto.UpdateUserDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.model.Tariff;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.projections.InvoiceByUser;
import edu.utn.utnphones.service.*;
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

import static edu.utn.utnphones.model.User.Status.active;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/backoffice")
public class BackOfficeWebController {

    private final SessionManager sessionManager;
    private final CallService callService;
    private final UserService userService;
    private final PhoneLineService phoneLineService;
    private final TariffService tariffService;
    private final InvoiceService invoiceService;

    @Autowired
    public BackOfficeWebController(UserService userService, CallService callService, PhoneLineService phoneLineService,
                                   TariffService tariffService, InvoiceService invoiceService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.callService = callService;
        this.tariffService = tariffService;
        this.phoneLineService = phoneLineService;
        this.invoiceService = invoiceService;
    }

    //USERS

    @ResponseStatus(OK)
    @PostMapping("/user")
    public ResponseEntity<URI> newUser(@RequestHeader("Authorization") String sessionToken, @RequestBody User user)
            throws InvalidRequestException, RecordAlreadyExistsException {
        return ResponseEntity.created(RestUtils.getLocation(userService.addUser(user))).build();
    }

    @ResponseStatus(OK)
    @GetMapping("/user")
    public ResponseEntity getById(@RequestHeader("Authorization") String sessionToken, @RequestParam(value = "userId", required = true) Integer userId) throws RecordNotExistsException {
        return ResponseEntity.status(OK).body(userService.findById(userId));
    }

    @ResponseStatus(OK)
    @DeleteMapping("/user")
    public ResponseEntity disableUser(@RequestHeader("Authorization") String sessionToken, @RequestParam("userId") Integer userId) throws RecordNotExistsException {
        return ResponseEntity.status(OK).body(userService.disableUser(userId));
    }

    @ResponseStatus(OK)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String sessionToken) {
        List<User> temp = userService.getAll();
        List<User> users = new ArrayList<>();
        for (User user : temp)
            if (user.getUserStatus().equals(active))
                users.add(user);
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ResponseStatus(OK)
    @PutMapping("/user")
    public ResponseEntity<URI> updateUser(@RequestHeader("Authorization") String sessionToken,
                                          @RequestParam(value = "userId", required = true) Integer userId,
                                          @RequestBody UpdateUserDto updatedUser) throws RecordNotExistsException, RecordAlreadyExistsException {
        return ResponseEntity.status(OK).body(RestUtils.getLocation(userService.updateUser(userId, updatedUser)));
    } // todo no funciona

    //PHONELINES

    @GetMapping("/phoneline")
    public ResponseEntity getPhonelineById(@RequestParam(name = "id_phone_line", required = false) Integer phoneLineId) {
        List<PhoneLine> phonelines = phoneLineService.getAll(phoneLineId);
        if (isNull(phoneLineId))
            return (phonelines.size() > 0) ? ResponseEntity.ok(phonelines) : ResponseEntity.noContent().build();
        return (phonelines.size() > 0) ? ResponseEntity.ok(phonelines.get(0)) : ResponseEntity.notFound().build();
    }

    @PostMapping("/phoneline")
    public ResponseEntity<URI> addPhoneline(@RequestHeader("Authorization") String sessionToken, @RequestBody PhoneLine phoneline) throws RecordAlreadyExistsException {
        return ResponseEntity.created(RestUtils.getLocation(phoneLineService.addPhoneLine(phoneline))).build();
    }//todo correcciones

    @PutMapping("/phoneline")
    public ResponseEntity actionPhoneLine(@RequestHeader("Authorization") String sessionToken, @RequestBody PhoneLineActionRequest action) throws RecordNotExistsException {
        phoneLineService.updateStatus(action.getStatus().toString(), action.getPhoneLineId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phoneline")
    public ResponseEntity disablePhoneLine(@RequestHeader("Authorization") String sessionToken, @RequestParam("idPhoneLine") Integer idPhoneLine) throws RecordNotExistsException {
        phoneLineService.updateStatus(PhoneLine.Status.disabled.toString(), idPhoneLine);
        return ResponseEntity.ok().build();
    }

    //TARIFFS

    @GetMapping("/tariffs")
    public ResponseEntity<List<Tariff>> getTariffs() {
        List<Tariff> tariffs = tariffService.getAll();
        return (tariffs.size() > 0) ? ResponseEntity.ok(tariffs) : ResponseEntity.noContent().build();
    }

    //CALLS
    @GetMapping("/calls")
    public ResponseEntity<List<Call>> getCallsByUser(@RequestHeader("Authorization") String sessionToken,
                                                     @RequestParam("userId") Integer userId) throws InvalidRequestException {
        List<Call> calls = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (isNull(userId))
            throw new InvalidRequestException("Please provide id user (userId)");
        calls = callService.getCallsByUser(userId);
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
            if (isNull(userId))
                throw new InvalidRequestException("Please provide id user (userId)");
            if (!isNull(from) && !isNull(to)) {
                Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
                Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
                calls = callService.getCallsByDates(userId, fromDate, toDate);
            } else
                throw new InvalidRequestException("Please provide service with dates 'from' and 'to'");
            return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ParseException ex) {
            throw new InvalidRequestException("Something went wrong when parsing dates, please provide dates with format: dd/MM/yyyy");
        }
    }

    //INVOICES

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceByUser>> getInvoicesByUser(@RequestHeader("Authorization") String sessionToken,
                                                                 @RequestParam("userId") Integer userId) throws InvalidRequestException {
        List<InvoiceByUser> invoices = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (isNull(userId))
            throw new InvalidRequestException("Please provide id user (userId)");
        invoices = invoiceService.getInvoicesByUser(userId);
        return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/invoicesByDates")
    public ResponseEntity<List<InvoiceByUser>> getInvoicesByDates(@RequestHeader("Authorization") String sessionToken,
                                                                  @RequestParam("userId") Integer userId,
                                                                  @RequestParam("from") String from,
                                                                  @RequestParam("to") String to) throws InvalidRequestException {
        List<InvoiceByUser> invoices = new ArrayList<>();
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        try {
            if (isNull(userId))
                throw new InvalidRequestException("Please provide id user (userId)");
            if (!isNull(from) && !isNull(to)) {
                Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
                Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
                invoices = invoiceService.getInvoicesByDates(userId, fromDate, toDate);
            } else
                throw new InvalidRequestException("Please provide service with dates 'from' and 'to'");
            return (invoices.size() > 0) ? ResponseEntity.ok(invoices) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ParseException ex) {
            throw new InvalidRequestException("Something went wrong when parsing dates, please provide dates with format: dd/MM/yyyy");
        }
    }
}
