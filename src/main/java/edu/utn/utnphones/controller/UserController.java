package edu.utn.utnphones.controller;


import edu.utn.utnphones.exceptions.NoCallsException;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostDurationProjection;
import edu.utn.utnphones.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserbyId(@PathVariable Integer userId) throws UserNotExistsException {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping("/")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        return ResponseEntity.ok(userService.addUser(newUser));
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll(@RequestParam(required = false) String name) {
        List<User> users= new ArrayList<>();
        users= userService.getAll(name);
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.noContent().build();
    }


    @GetMapping("/mostDuration")
    public MostDurationProjection getMostDurationFromUser () throws NoCallsException {
        return userService.getMostDurationFromUser();
    }
}
