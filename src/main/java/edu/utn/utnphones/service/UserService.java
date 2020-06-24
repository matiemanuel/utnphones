package edu.utn.utnphones.service;

import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static edu.utn.utnphones.model.User.Status.disabled;
import static java.util.Objects.isNull;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) throws UserNotExistsException {
        User user = userRepository.getByEmailAndPassword(email, password);
        return Optional.ofNullable(user).orElseThrow(() -> new UserNotExistsException());
    }

    public User addUser(User newUser) {
        return (userRepository.save(newUser));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) throws UserNotExistsException {
        return userRepository.findById(id).orElseThrow(UserNotExistsException::new);
    }

    public User disableUser(Integer userId) throws UserNotExistsException {
        User user = findById(userId);
        user.setUser_status(disabled);
        return userRepository.save(user);
    }

  /*  public User updateUser(User user) throws UserNotExistsException {
        if (userRepository.save(user) != null) {
            return user;
        } else {
            throw new UserNotExistsException();
        }
    }*/

    public MostCalledProjection getMostCalledFromUser(Integer userId) {
        return userRepository.getMostCalledFromUser(userId);
    }
}
