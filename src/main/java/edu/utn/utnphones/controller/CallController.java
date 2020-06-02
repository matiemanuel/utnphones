package edu.utn.utnphones.controller;

import edu.utn.utnphones.exceptions.CallNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @GetMapping("/getByDates/")
    public ResponseEntity<List<Call>> getCallsByDates(@RequestParam String origin_number,
                                                      @RequestParam String from,
                                                      @RequestParam String to) throws ParseException {
        List<Call> calls = new ArrayList<>();
        if ((from != null) && (to != null)){
            Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(to);
            calls = callService.getBetweenDates(origin_number, fromDate, toDate);
        }
        return (calls.size() > 0) ? ResponseEntity.ok(calls) : ResponseEntity.noContent().build();
    }
}
