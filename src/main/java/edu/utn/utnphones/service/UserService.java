package edu.utn.utnphones.service;

import edu.utn.utnphones.dto.UpdateUserDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.MostCalledProjection;
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

    public User login(String email, String password) throws RecordNotExistsException {
        User user = userRepository.getByEmailAndPassword(email, password);
        return Optional.ofNullable(user).orElseThrow(() -> new RecordNotExistsException("Login problems, please check the data provided"));
    }

    public User addUser(User newUser) throws InvalidRequestException, RecordAlreadyExistsException {
        if (isNull(newUser))
            throw new InvalidRequestException("You need to provide the new user info");
        User saved = userRepository.save(newUser);
        if (isNull(saved))
            throw new RecordAlreadyExistsException("Email is already added");
        return saved;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) throws RecordNotExistsException {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotExistsException("No user found with provided Id"));
    }

    public User disableUser(Integer userId) throws RecordNotExistsException {
        User user = findById(userId);
        user.setUserStatus(disabled);
        return userRepository.save(user);
    }

    public User updateUser(Integer id, UpdateUserDto newUserData) throws RecordNotExistsException, RecordAlreadyExistsException {
        Optional<User> userById = userRepository.findById(id);
        if(!userById.isPresent())
            throw new RecordNotExistsException("Id provided is not exists on users data");
        if(!isNull(userRepository.findByEmail(newUserData.getEmail()))){
            throw new RecordAlreadyExistsException("Email already exists, please use another");
        }
        User updated = userById.get();
        updated.updateUser(newUserData);
        userRepository.updateUser(updated.getName(), updated.getLastname(), updated.getDni(), updated.getPassword(),
                updated.getCity().getId(), updated.getUserType().toString(), updated.getUserStatus().toString(),
                updated.getEmail());
        return updated;
    }

    public MostCalledProjection getMostCalledFromUser(Integer userId) throws InvalidRequestException {
        return userRepository.getMostCalledFromUser(userId).orElseThrow(() -> new InvalidRequestException("No user found with provided id" +
                "or it doesn't make any call yet"));
    }
}
