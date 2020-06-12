package edu.utn.utnphones.controller.web;



import edu.utn.utnphones.dto.CallRequestDto;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.service.UserService;
import edu.utn.utnphones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backoffice/")
public class BackOfficeWebController {

    private final SessionManager sessionManager;
    private final CallService callService;
    private final UserService userService;

    @Autowired
    public BackOfficeWebController(UserService userService,CallService callService, SessionManager sessionManager) {
        this.userService= userService;
        this.sessionManager = sessionManager;
        this.callService= callService;
    }

    //No funciona - tira exception sql REVISAR
    @PostMapping("/addCall")
    public ResponseEntity newCall(@RequestHeader("Authorization") String sessionToken,@RequestBody CallRequestDto callDto) {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(callService.addCallFromBackOffice(callDto.getOrigin(),
                callDto.getDestiny(), callDto.getDuration()));

    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("users")
    public List<User> getAll(@RequestParam(required = false) String name) {
        return userService.getAll(name);
    }

}
