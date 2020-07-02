package edu.utn.utnphones.service;

import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Tariff;
import edu.utn.utnphones.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public void addTariff(Tariff newTariff) {
        tariffRepository.save(newTariff);
    }

    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }

    public Tariff findById(Integer id) throws RecordNotExistsException {
        return tariffRepository.findById(id).orElseThrow(() -> new RecordNotExistsException("Tariff id provided doesn't exist"));
    }
}
