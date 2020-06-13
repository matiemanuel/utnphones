package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.CallNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class CallService {

    private final CallRepository callRepository;

    @Autowired
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public Call addCall(Call newCall) {
        return callRepository.save(newCall);
    }

    public Call addCallFromBackOffice(String origin_number, String destiny_number, Integer duration) throws CallNotExistsException {
         Integer id = callRepository.addCall(origin_number, destiny_number, duration);
         return this.findById(id);
    }

    public List<Call> getAll(String origin_number) {
        if (isNull(origin_number)) {
            return callRepository.findAll();
        }
        return callRepository.findbyOriginNumber(origin_number);
    }

    public Call findById(Integer id) throws CallNotExistsException {
        return callRepository.findById(id).orElseThrow(CallNotExistsException::new);
    }
}
