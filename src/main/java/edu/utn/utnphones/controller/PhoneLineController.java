package edu.utn.utnphones.controller;

import edu.utn.utnphones.exceptions.PhoneLineNotExistsException;
import edu.utn.utnphones.model.City;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.service.PhoneLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone_lines")
public class PhoneLineController {

    private final PhoneLineService phonelineService;

    @Autowired
    public PhoneLineController(PhoneLineService phonelineService) {
        this.phonelineService = phonelineService;
    }

    @GetMapping("/{phonelineId}")
    public PhoneLine getPhoneLinebyId(@PathVariable Integer phonelineId) throws PhoneLineNotExistsException {
        return this.phonelineService.findById(phonelineId);
    }

    @PostMapping("/")
    public void addPhoneLine(@RequestBody PhoneLine newPhoneLine) {// transforma el json en una entidad
        phonelineService.addPhoneLine(newPhoneLine);
    }

    @GetMapping("/")
    public List<PhoneLine> getAll(@RequestParam(required = false) String lineNumber) {
        return phonelineService.getAll(lineNumber);
    }
}
