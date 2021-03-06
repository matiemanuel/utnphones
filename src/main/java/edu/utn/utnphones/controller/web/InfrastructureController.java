package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.NewCallRequestDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/infrastructure")
public class InfrastructureController {

    private CallService callService;

    private RestUtils restUtils;

    @Autowired
    public InfrastructureController(CallService callService, RestUtils restUtils) {
        this.callService = callService;
        this.restUtils = restUtils;
    }

    @GetMapping("/")
    public ResponseEntity<Call> getCallById(@RequestParam("id_call") Integer idCall) throws RecordNotExistsException {
        return ResponseEntity.ok(callService.findById(idCall));
    }

    @PostMapping("/")
    public ResponseEntity<URI> addPhoneCall(@RequestBody NewCallRequestDto newCall) throws InvalidRequestException, RecordNotExistsException {
        Call call = callService.addCall(newCall.getOrigin(), newCall.getDestiny(), newCall.getDuration());
        return ResponseEntity.created(restUtils.getLocation(call)).build();
    }
}
