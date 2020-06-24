package edu.utn.utnphones.controllers.web.backofficewebcontroller;

import edu.utn.utnphones.controller.web.BackOfficeWebController;
import edu.utn.utnphones.dto.PhoneLineActionRequest;
import edu.utn.utnphones.exceptions.PhoneLineNotExistsException;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.service.PhoneLineService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static edu.utn.utnphones.model.PhoneLine.Status.*;
import static edu.utn.utnphones.model.PhoneLine.Type.mobile;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackOfficePhoneLineServicesTests {

    private BackOfficeWebController controller;

    private PhoneLineService phoneLineService;

    private String token = "token";

    private PhoneLine activePhoneLine = new PhoneLine(1,"123123",null,null, active, mobile, null);

    private PhoneLine desactivatedPhoneLine = new PhoneLine(1,"123123",null,null, disabled, mobile, null);

    private PhoneLine suspendedPhoneLine = new PhoneLine(1,"123123",null,null, suspended, mobile, null);

    private PhoneLine disabledPhoneLine = new PhoneLine(2,"123123",null,null, disabled, mobile, null);

    private PhoneLineActionRequest statusUpdateRequest = new PhoneLineActionRequest(0, null);

    @SneakyThrows
    @Before
    public void init() throws UserNotExistsException {
        phoneLineService = mock(PhoneLineService.class);
        controller = new BackOfficeWebController(null, null, phoneLineService, null, null, null);
        when(phoneLineService.addPhoneLine(activePhoneLine)).thenReturn(activePhoneLine);
        when(phoneLineService.updateStatus(disabled.toString(), 1)).thenReturn(desactivatedPhoneLine);
        when(phoneLineService.updateStatus(suspended.toString(), 1)).thenReturn(suspendedPhoneLine);
        when(phoneLineService.updateStatus(active.toString(), -1)).thenThrow(PhoneLineNotExistsException.class);
    }

    @Test
    public void createPhoneLine() {
        ResponseEntity response = controller.addPhoneline(token, activePhoneLine);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test
    public void phoneLineAction() throws PhoneLineNotExistsException {
        statusUpdateRequest.setPhoneLineId(1);
        statusUpdateRequest.setStatus(disabled);
        ResponseEntity response = controller.actionPhoneLine(token, statusUpdateRequest);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test(expected = PhoneLineNotExistsException.class)
    public void phoneLineActionNotValidId() throws PhoneLineNotExistsException {
        statusUpdateRequest.setPhoneLineId(-1);
        statusUpdateRequest.setStatus(active);
        controller.actionPhoneLine(token, statusUpdateRequest);
    }

    @Test
    public void disablePhoneline() throws PhoneLineNotExistsException {
        ResponseEntity response = controller.disablePhoneLine(token, 1);
        assertEquals(SC_OK, response.getStatusCodeValue());
    }

    @Test (expected = PhoneLineNotExistsException.class)
    public void disablePhoneLineWithInvalidId() throws PhoneLineNotExistsException {
        controller.disablePhoneLine(token, -1);
    }
}
