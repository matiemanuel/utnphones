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

    public User updateUser(Integer id, UpdateUserDto newUserData) throws RecordNotExistsException {
        Optional<User> userById = userRepository.findById(id);
        if(!userById.isPresent())
            throw new RecordNotExistsException("Id provided is not exists on users data");
        User updated = userById.get();
        updated.updateUser(newUserData);
        userRepository.updateUser(updated.getName(), updated.getLastname(), updated.getPassword(),
                updated.getCity().getId(), updated.getUserType().toString(), updated.getUserStatus().toString(),
                updated.getEmail());
        return updated;
    }

    public List<MostCalledProjection> getMostCalledFromUser(Integer userId, Integer size) throws InvalidRequestException {
        List<MostCalledProjection> mostCalledFromUser = userRepository.getMostCalledFromUser(userId, size);
        if (!isNull(mostCalledFromUser))
            return mostCalledFromUser;
        throw new InvalidRequestException("No user found with provided id");
    }
}
