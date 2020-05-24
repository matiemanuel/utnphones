package edu.utn.utnphones.controller;

import edu.utn.utnphones.exceptions.CallNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final CallService callService;

    @Autowired
    public CallController(CallService callService) {
        this.callService = callService;
    }

    @GetMapping("/{callId}")
    public Call getCallbyId(@PathVariable Integer callId) throws CallNotExistsException {
        return this.callService.findById(callId);
    }

    @PostMapping("/")
    public void addCall(@RequestBody Call newCall) {// transforma el json en una entidad
        callService.addCall(newCall);
    }

    @GetMapping("/")
    public List<Call> getAll(@RequestParam(required = false) String origin_number) {
        return callService.getAll(origin_number);
    }
}
