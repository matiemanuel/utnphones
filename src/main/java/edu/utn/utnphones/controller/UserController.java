package edu.utn.utnphones.controller;


import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.exceptions.ValidationException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User login(String username, String password) throws RecordNotExistsException, ValidationException {
        if ((username != null) && (password != null)) {
            return userService.login(username, password);
        } else {
            throw new ValidationException("username and password must have a value");
        }
    }

    @GetMapping("/{userId}")
    public User getUserbyId(@PathVariable Integer userId) throws RecordNotExistsException {
        return this.userService.findById(userId);
    }

    @PostMapping("/")
    public User addUser(@RequestBody User newUser) throws InvalidRequestException, RecordAlreadyExistsException {
        return userService.addUser(newUser);
    }

    @GetMapping("/")
    public List<User> getAll() {
        return userService.getAll();
    }
}
