package edu.utn.utnphones.service;

import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public List<User> getAll(String name) {
        if (isNull(name)) {
            return userRepository.findAll();
        }
        return userRepository.findbyName(name);
    }

    public User findById(Integer id) throws UserNotExistsException {
        return userRepository.findById(id).orElseThrow(UserNotExistsException::new);
    }

    public MostCalledProjection getMostCalledFromUser(Integer userId) {
        return userRepository.getMostCalledFromUser(userId);
    }
}
