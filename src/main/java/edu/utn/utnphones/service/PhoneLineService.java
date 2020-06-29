package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.repository.PhoneLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class PhoneLineService {

    private final PhoneLineRepository phonelineRepository;

    @Autowired
    public PhoneLineService(PhoneLineRepository phonelineRepository) {
        this.phonelineRepository = phonelineRepository;
    }

    public PhoneLine addPhoneLine(PhoneLine newPhoneLine) throws RecordAlreadyExistsException {
        PhoneLine saved = phonelineRepository.save(newPhoneLine);
        if (isNull(saved)) {
            throw new RecordAlreadyExistsException("Number phoneline is already added");
        }
        return saved;
    }

    public List<PhoneLine> getAll(Integer phoneLineId) {
        ArrayList<PhoneLine> phonelines = new ArrayList<>();
        if (isNull(phoneLineId)) {
            return phonelineRepository.findAll();
        }
        Optional<PhoneLine> byId = phonelineRepository.findById(phoneLineId);
        if (byId.isPresent()){
            phonelines.add(byId.get());
            return phonelines;
        }
        return phonelines;
    }

    public PhoneLine findById(Integer id) throws RecordNotExistsException {
        return phonelineRepository.findById(id).orElseThrow(() -> new RecordNotExistsException("Phoneline requested doesn't exists"));
    }

    public PhoneLine updateStatus(String status, Integer idPhoneLine) throws RecordNotExistsException {
        PhoneLine pl = this.findById(idPhoneLine);
        phonelineRepository.updateStatus(status, idPhoneLine);
        return pl;
    }
}
