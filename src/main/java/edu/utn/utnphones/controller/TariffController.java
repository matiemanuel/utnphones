package edu.utn.utnphones.controller;


import edu.utn.utnphones.exceptions.TariffNotExistsException;
import edu.utn.utnphones.model.Tariff;
import edu.utn.utnphones.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping("/{tariffId}")
    public Tariff getTariffbyId(@PathVariable Integer tariffId) throws TariffNotExistsException {
        return this.tariffService.findById(tariffId);
    }

    @PostMapping("/")
    public void addTariff(@RequestBody Tariff newTariff) {// transforma el json en una entidad
        tariffService.addTariff(newTariff);
    }

    @GetMapping("/")
    public List<Tariff> getAll() {
        return tariffService.getAll();
    }

}
