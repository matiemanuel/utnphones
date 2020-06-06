package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.controller.UserController;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserWebController {
    private final UserController userController;
    private final SessionManager sessionManager;

    @Autowired
    public UserWebController(UserController userController, SessionManager sessionManager) {
        this.userController = userController;
        this.sessionManager = sessionManager;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String sessionToken) {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<User> users = userController.getAll(currentUser.getName());
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/add")
    public ResponseEntity newUser(@RequestHeader("Authorization") String sessionToken, @RequestBody User user ) throws UserNotExistsException {
        User currentUser = sessionManager.getCurrentUser(sessionToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(userController.addUser(user));

    }
}
