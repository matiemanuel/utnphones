package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.PhoneLineActionRequest;
import edu.utn.utnphones.exceptions.PhoneLineNotExistsException;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.*;
import edu.utn.utnphones.service.*;
import edu.utn.utnphones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

import static edu.utn.utnphones.model.User.Status.active;
import static edu.utn.utnphones.model.User.Status.disabled;
import static org.springframework.http.HttpStatus.CREATED;
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

    //USERS

    @ResponseStatus(OK)
    @PostMapping("/user")
    public ResponseEntity newUser(@RequestHeader("Authorization") String sessionToken, @RequestBody User user ){
        return ResponseEntity.status(CREATED).body(userService.addUser(user));
    }

    @ResponseStatus(OK)
    @GetMapping("/user")
    public ResponseEntity getById(@RequestHeader("Authorization") String sessionToken, @RequestParam("userId") Integer userId ) throws UserNotExistsException {
        return ResponseEntity.status(OK).body(userService.findById(userId));
    }

    @ResponseStatus(OK)
    @DeleteMapping("/user")
    public ResponseEntity disableUser(@RequestHeader("Authorization") String sessionToken, @RequestParam("userId") Integer userId ) throws UserNotExistsException {
        return ResponseEntity.status(OK).body(userService.disableUser(userId));
    }

    @ResponseStatus(OK)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String sessionToken) {
        List<User> temp = userService.getAll();
        List<User> users = new ArrayList<>();
        for(User user : temp)
            if(user.getUser_status().equals(active))
                users.add(user);
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //PHONELINES
    @ResponseStatus(OK)
    @PostMapping("/phoneline")
    public ResponseEntity addPhoneline(@RequestHeader("Authorization") String sessionToken, @RequestBody PhoneLine phoneline) {
        return ResponseEntity.status(CREATED).body(phoneLineService.addPhoneLine(phoneline));
    }

    @PutMapping("/phoneline")
    public ResponseEntity phoneLineAction(@RequestHeader("Authorization") String sessionToken, @RequestBody PhoneLineActionRequest action) throws PhoneLineNotExistsException {
        phoneLineService.updateStatus(action.getStatus().toString(), action.getPhoneLineId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phoneline")
    public ResponseEntity phoneLineAction(@RequestHeader("Authorization") String sessionToken, @RequestParam("idPhoneLine") Integer idPhoneLine) throws PhoneLineNotExistsException {
        phoneLineService.updateStatus(PhoneLine.Status.disabled.toString(), idPhoneLine);
        return ResponseEntity.ok().build();
    }





}
