package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.PhoneLineActionRequest;
import edu.utn.utnphones.dto.UpdateUserDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.*;
import edu.utn.utnphones.session.SessionManager;
import edu.utn.utnphones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static edu.utn.utnphones.model.User.Status.active;
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
    public ResponseEntity<URI> newUser(@RequestHeader("Authorization") String sessionToken, @RequestBody User user) throws InvalidRequestException, RecordAlreadyExistsException {
        return (ResponseEntity<URI>) ResponseEntity.status(CREATED).body(RestUtils.getLocation(userService.addUser(user)));
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
    }

    //PHONELINES

    @GetMapping("/phoneline")
    public ResponseEntity<List<PhoneLine>> getPhoneline() {
        List<PhoneLine> phonelines = phoneLineService.getAll(null);
        return (phonelines.size()>0) ? ResponseEntity.ok(phonelines) : ResponseEntity.noContent().build();
    }

    @PostMapping("/phoneline")
    public ResponseEntity<URI> addPhoneline(@RequestHeader("Authorization") String sessionToken, @RequestBody PhoneLine phoneline) throws RecordAlreadyExistsException {
        return ResponseEntity.status(CREATED).body(RestUtils.getLocation(phoneLineService.addPhoneLine(phoneline)));
    }

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
}
