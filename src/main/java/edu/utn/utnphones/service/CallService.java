package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public Call addCall(String origin_number, String destiny_number, Integer duration) throws InvalidRequestException, RecordNotExistsException {
        Integer id = callRepository.addCall(origin_number, destiny_number, duration);
        if (id == null) {
            throw new InvalidRequestException("Call can not be created");
        }
        return this.findById(id);
    }

    public List<Call> getAll(String origin_number) {
        if (isNull(origin_number)) {
            return callRepository.findAll();
        }
        return callRepository.findbyOriginNumber(origin_number);
    }

    public Call findById(Integer id) throws RecordNotExistsException {
        return callRepository.findById(id).orElseThrow(() -> new RecordNotExistsException("Id provided is not valid for a phoneline"));
    }

    public List<CallsByDates> getCallsByDates(Integer idUser, Date from, Date to) {
        return callRepository.getCallsByDates(idUser, from, to);
    }

    public List<Call> getCallsByUser(Integer idUser) {
        return callRepository.getCallsByUser(idUser);
    }
}
