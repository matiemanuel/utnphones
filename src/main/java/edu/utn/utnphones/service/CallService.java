package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.CallNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CallService {

    private final CallRepository callRepository;

    @Autowired
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public void addCall(Call newCall) {
        callRepository.save(newCall);
    }

    public List<Call> getAll(String origin_number) {
        if (isNull(origin_number)) {
            return callRepository.findAll();
        }
        return callRepository.findbyOriginNumber(origin_number);
    }
    public List<Call> getBetweenDates(String origin_number, Date from, Date to) {
        return callRepository.getByOriginNumberAndDates(origin_number, from, to);
    }
    public Call findById(Integer id) throws CallNotExistsException {
        return callRepository.findById(id).orElseThrow(CallNotExistsException::new);
    }
}
