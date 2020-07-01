package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.web.InfrastructureController;
import edu.utn.utnphones.dto.NewCallRequestDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.utils.RestUtils;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InfrastructureControllerTests {

    private InfrastructureController infrastructureController;

    private CallService callService;
    private RestUtils rest;

    private String validNumberOrigyn = "2235123123";
    private String invalidNumberOrigyn = "2235ab3123";
    private String validNumberDestiny = "2235123321";
    private String invalidNumberDestiny = "223ab51231";

    Call callTest = new Call(1, null, 60, validNumberOrigyn,
            validNumberDestiny, 2., 5., 300., "01/01/2020");

    @SneakyThrows
    @Before
    public void init() {
        callService = mock(CallService.class);
        rest = mock(RestUtils.class);
        when(callService.findById(-1)).thenThrow(RecordNotExistsException.class);
        when(callService.findById(1)).thenReturn(callTest);
        when(callService.addCall(validNumberOrigyn, validNumberDestiny, 60)).thenReturn(callTest);
        when(callService.addCall(validNumberOrigyn, invalidNumberDestiny, 60)).thenThrow(InvalidRequestException.class);
        when(callService.addCall(invalidNumberOrigyn, invalidNumberDestiny, 60)).thenThrow(RecordNotExistsException.class);
        when(rest.getLocation(callTest)).thenReturn(new URI("http://localhost:8080/infrastructure/calls?id=1"));

        infrastructureController = new InfrastructureController(callService, rest);
    }

    @Test(expected = RecordNotExistsException.class)
    public void callNotFoundTest() throws RecordNotExistsException {
        infrastructureController.getCallById(-1);
    }

    @Test
    public void callFoundTest() throws RecordNotExistsException {
        ResponseEntity<Call> callById = infrastructureController.getCallById(1);
        assertEquals(SC_OK, callById.getStatusCodeValue());
    }

    @Test
    public void addCallOk() throws InvalidRequestException, RecordNotExistsException, URISyntaxException {
        ResponseEntity<URI> uriResponseEntity = infrastructureController.addPhoneCall(new NewCallRequestDto(validNumberOrigyn,
                validNumberDestiny, 60));
        assertEquals(SC_CREATED, uriResponseEntity.getStatusCodeValue());
    }
}
