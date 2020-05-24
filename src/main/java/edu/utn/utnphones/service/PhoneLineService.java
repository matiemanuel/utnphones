package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.PhoneLineNotExistsException;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.repository.PhoneLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class PhoneLineService {

    private final PhoneLineRepository phonelineRepository;

    @Autowired
    public PhoneLineService(PhoneLineRepository phonelineRepository) {
        this.phonelineRepository = phonelineRepository;
    }

    public void addPhoneLine(PhoneLine newPhoneLine) {
        phonelineRepository.save(newPhoneLine);
    }

    public List<PhoneLine> getAll(String lineNumber) {
        if (isNull(lineNumber)) {
            return phonelineRepository.findAll();
        }
        return phonelineRepository.findbyLineNumber(lineNumber);
    }

    public PhoneLine findById(Integer id) throws PhoneLineNotExistsException {
        return phonelineRepository.findById(id).orElseThrow(PhoneLineNotExistsException::new);
    }
}
