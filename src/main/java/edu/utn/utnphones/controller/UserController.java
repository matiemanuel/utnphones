package edu.utn.utnphones.controller;


import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostCalledProjection;
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

    @GetMapping("/{userId}")
    public User getUserbyId(@PathVariable Integer userId) throws UserNotExistsException {// parametro de la url
        return this.userService.findById(userId);
    }

    @PostMapping("/")
    public void addUser(@RequestBody User newUser) {// transforma el json en una entidad
        userService.addUser(newUser);
    }

    @GetMapping("/")
    public List<User> getAll(@RequestParam(required = false) String name) {
        return userService.getAll(name);
    }

    @GetMapping("/{userId}/mostCalled")
    public MostCalledProjection getMostCalledFromUser(@PathVariable Integer userId) {
        return userService.getMostCalledFromUser(userId);
    }
}
