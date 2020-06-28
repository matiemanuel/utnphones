package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.dto.NewCallRequestDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/infrastructure")
public class InfrastructureController {

    @Autowired
    private CallService callService;

    public InfrastructureController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/")
    public ResponseEntity addPhoneCall(@RequestBody @Valid NewCallRequestDto newCall) throws InvalidRequestException, RecordNotExistsException {
        Call call = callService.addCall(newCall.getOrigin(), newCall.getDestiny(), newCall.getDuration());
        return ResponseEntity.created(RestUtils.getLocation(call)).build();
    }
}
