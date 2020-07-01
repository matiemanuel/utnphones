package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.web.BackOfficeWebController;
import edu.utn.utnphones.dto.PhoneLineActionRequest;
import edu.utn.utnphones.dto.UpdateUserDto;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.*;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.projections.InvoiceByUser;
import edu.utn.utnphones.service.*;
import edu.utn.utnphones.session.SessionManager;
import edu.utn.utnphones.utils.RestUtils;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.utnphones.model.PhoneLine.Type.mobile;
import static edu.utn.utnphones.model.User.Status.active;
import static edu.utn.utnphones.model.User.Status.disabled;
import static edu.utn.utnphones.model.User.Type.client;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackOfficeControllerTests {

    private SessionManager sessionManager;
    private CallService callService;
    private UserService userService;
    private PhoneLineService phoneLineService;
    private TariffService tariffService;
    private InvoiceService invoiceService;
    private RestUtils rest;
    private BackOfficeWebController backOfficeWebController;
    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    private String token = "token";
    private User validUser;
    private User validUserUpdated;
    private List<User> userList = new ArrayList<>();
    private UpdateUserDto update = new UpdateUserDto();

    private List<PhoneLine> phonelines = new ArrayList<>();
    private List<PhoneLine> phonelinesEmpty = new ArrayList<>();
    private PhoneLine validPhoneline;
    private PhoneLine disabledValidPhoneline;
    private PhoneLine suspendedValidPhoneline;

    private List<Tariff> tariffs = new ArrayList<>();
    private List<Tariff> tariffsEmptyList = new ArrayList<>();

    private List<Call> calls = new ArrayList<>();
    private List<Call> callsEmptyList = new ArrayList<>();

    private List<CallsByDates> callsByDate = new ArrayList<>();
    private List<CallsByDates> callsByDateEmptyList = new ArrayList<>();

    private List<InvoiceByUser> invoices = new ArrayList<>();
    private List<InvoiceByUser> invoicesEmptyList = new ArrayList<>();

    private String validFrom = "01/01/2020";
    private String validTo = "05/01/2020";

    private Date validDateFrom;
    private Date validDateTo;


    @SneakyThrows
    @Before
    public void init() {
        validUser = new User(1, "test", "test", "test", "test", "test", null, client, active);
        validUserUpdated = new User(1, "testUpdated", "test", "test", "test", "test", null, client, active);

        validPhoneline = new PhoneLine(1, "2233123123", validUser, null, PhoneLine.Status.active, mobile, null);
        disabledValidPhoneline = new PhoneLine(1, "2233123123", validUser, null, PhoneLine.Status.disabled, mobile, null);
        suspendedValidPhoneline = new PhoneLine(1, "2233123123", validUser, null, PhoneLine.Status.suspended, mobile, null);

        phonelines.add(validPhoneline);

        callService = mock(CallService.class);
        invoiceService = mock(InvoiceService.class);
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);
        rest = mock(RestUtils.class);
        phoneLineService = mock(PhoneLineService.class);
        tariffService = mock(TariffService.class);

        userList.add(validUser);

        tariffs.add(new Tariff());

        calls.add(new Call());

        callsByDate.add(factory.createProjection(CallsByDates.class));

        invoices.add(factory.createProjection(InvoiceByUser.class));

        validDateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(validFrom);

        validDateTo = new SimpleDateFormat("dd/MM/yyyy").parse(validTo);

        when(userService.addUser(validUser)).thenReturn(validUser);
        when(rest.getLocation(validUser)).thenReturn(new URI("http://localhost:8080/test"));
        when(userService.findById(1)).thenReturn(validUser);
        when(userService.disableUser(1)).thenReturn(validUser);
        when(userService.getAll()).thenReturn(userList);
        when(userService.updateUser(1, update)).thenReturn(validUser);

        when(phoneLineService.getAll(1)).thenReturn(phonelines);
        when(phoneLineService.getAll(-1)).thenReturn(phonelinesEmpty);
        when(phoneLineService.getAll(null)).thenReturn(phonelinesEmpty);
        when(phoneLineService.addPhoneLine(validPhoneline)).thenReturn(validPhoneline);
        when(rest.getLocation(validPhoneline)).thenReturn(new URI("http://localhost:8080/test"));
        when(phoneLineService.updateStatus("disabled", 1)).thenReturn(disabledValidPhoneline);
        when(phoneLineService.updateStatus("suspended", 1)).thenReturn(suspendedValidPhoneline);

        when(tariffService.getAll()).thenReturn(tariffsEmptyList);

        when(callService.getCallsByUser(1)).thenReturn(calls);
        when(callService.getCallsByUser(2)).thenReturn(callsEmptyList);

        when(callService.getCallsByDates(1, validDateFrom, validDateTo)).thenReturn(callsByDate);
        when(callService.getCallsByDates(2, validDateFrom, validDateTo)).thenReturn(callsByDateEmptyList);

        when(invoiceService.getInvoicesByUser(1)).thenReturn(invoices);
        when(invoiceService.getInvoicesByUser(2)).thenReturn(invoicesEmptyList);

        when(invoiceService.getInvoicesByDates(1, validDateFrom, validDateTo)).thenReturn(invoices);
        when(invoiceService.getInvoicesByDates(2, validDateFrom, validDateTo)).thenReturn(invoicesEmptyList);


        backOfficeWebController = new BackOfficeWebController(userService, callService, phoneLineService,
                tariffService, invoiceService, sessionManager, rest);
    }


    @Test
    public void createUser() throws InvalidRequestException, RecordAlreadyExistsException {
        ResponseEntity<URI> uriResponseEntity = backOfficeWebController.newUser(token, validUser);
        assertEquals(SC_CREATED, uriResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getById() throws RecordNotExistsException {
        ResponseEntity byId = backOfficeWebController.getById(token, 1);
        assertEquals(SC_OK, byId.getStatusCodeValue());
    }

    @Test
    public void disableUser() throws RecordNotExistsException {
        ResponseEntity responseEntity = backOfficeWebController.disableUser(token, 1);
        assertEquals(SC_OK, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getUsers(){
        ResponseEntity<List<User>> users = backOfficeWebController.getUsers(token);
        assertEquals(SC_OK, users.getStatusCodeValue());

        validUser.setUserStatus(disabled);

        ResponseEntity<List<User>> usersEmpty = backOfficeWebController.getUsers(token);
        assertEquals(SC_NO_CONTENT, usersEmpty.getStatusCodeValue());
    }

    @SneakyThrows
    @Test
    public void updateUser(){
        ResponseEntity<URI> uriResponseEntity = backOfficeWebController.updateUser(token, 1, update);
        assertEquals(SC_OK, uriResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getPhones(){
        ResponseEntity phonelineById = backOfficeWebController.getPhoneline(1);
        assertEquals(SC_OK, phonelineById.getStatusCodeValue());

        ResponseEntity phonelineByNotFoundId = backOfficeWebController.getPhoneline(-1);
        assertEquals(SC_NOT_FOUND, phonelineByNotFoundId.getStatusCodeValue());

        ResponseEntity phonelineByNoContent = backOfficeWebController.getPhoneline(null);
        assertEquals(SC_NO_CONTENT, phonelineByNoContent.getStatusCodeValue());

        when(phoneLineService.getAll(null)).thenReturn(phonelines);

        ResponseEntity phonelineByWithContent = backOfficeWebController.getPhoneline(null);
        assertEquals(SC_OK, phonelineByWithContent.getStatusCodeValue());
    }

    @SneakyThrows
    @Test
    public void addPhoneLine(){
        ResponseEntity<URI> uriResponseEntity = backOfficeWebController.addPhoneline(token, validPhoneline);
        assertEquals(SC_CREATED, uriResponseEntity.getStatusCodeValue());
    }

    @SneakyThrows
    @Test
    public void updatePhoneLines(){
        PhoneLineActionRequest disableReq = new PhoneLineActionRequest(1, PhoneLine.Status.disabled);
        ResponseEntity responseEntity = backOfficeWebController.actionPhoneLine(token, disableReq);
        assertEquals(SC_OK, responseEntity.getStatusCodeValue());

        ResponseEntity responseEntity2 = backOfficeWebController.disablePhoneLine(token, 1);
        assertEquals(SC_OK, responseEntity2.getStatusCodeValue());
    }

    @Test
    public void getTariffs(){

        ResponseEntity<List<Tariff>> emptyResponse = backOfficeWebController.getTariffs();
        assertEquals(SC_NO_CONTENT, emptyResponse.getStatusCodeValue());

        when(tariffService.getAll()).thenReturn(this.tariffs);

        ResponseEntity<List<Tariff>> listResponse = backOfficeWebController.getTariffs();
        assertEquals(SC_OK, listResponse.getStatusCodeValue());
    }

    @Test(expected = InvalidRequestException.class)
    public void getCallsByUser() throws InvalidRequestException {
        ResponseEntity<List<Call>> okResponse = backOfficeWebController.getCallsByUser(token, 1);
        assertEquals(SC_OK, okResponse.getStatusCodeValue());
        ResponseEntity<List<Call>> noContentResponse = backOfficeWebController.getCallsByUser(token, 2);
        assertEquals(SC_NO_CONTENT, noContentResponse.getStatusCodeValue());
        backOfficeWebController.getCallsByUser(token, null);
    }

    @Test
    public void getCallsByDates() throws InvalidRequestException {
        try {
            backOfficeWebController.getCallsByDates(token, null, "", "");
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getCallsByDates(token, 1, "abc", "213");
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getCallsByDates(token, 1, null, null);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getCallsByDates(token, 1, validFrom, null);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getCallsByDates(token, 1, null, validTo);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        ResponseEntity<List<CallsByDates>> okResponse = backOfficeWebController.getCallsByDates(token, 1, validFrom, validTo);
        assertEquals(SC_OK, okResponse.getStatusCodeValue());

        ResponseEntity<List<CallsByDates>> noContentResponse = backOfficeWebController.getCallsByDates(token, 2, validFrom, validTo);
        assertEquals(SC_NO_CONTENT, noContentResponse.getStatusCodeValue());
    }

    @Test(expected = InvalidRequestException.class)
    public void getInvoicesByUser() throws InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> okResponse = backOfficeWebController.getInvoicesByUser(token, 1);
        assertEquals(SC_OK, okResponse.getStatusCodeValue());
        ResponseEntity<List<InvoiceByUser>> noContentResponse = backOfficeWebController.getInvoicesByUser(token, 2);
        assertEquals(SC_NO_CONTENT, noContentResponse.getStatusCodeValue());
        backOfficeWebController.getInvoicesByUser(token, null);
    }

    @Test
    public void getInvoicesByDates() throws InvalidRequestException {
        try {
            backOfficeWebController.getInvoicesByDates(token, null, "", "");
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getInvoicesByDates(token, 1, "abc", "213");
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getInvoicesByDates(token, 1, null, null);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getInvoicesByDates(token, 1, validFrom, null);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        try {
            backOfficeWebController.getInvoicesByDates(token, 1, null, validTo);
            assertTrue(false);
        } catch (InvalidRequestException e) {
            assertTrue(true);
        }

        ResponseEntity<List<InvoiceByUser>> okResponse = backOfficeWebController.getInvoicesByDates(token, 1, validFrom, validTo);
        assertEquals(SC_OK, okResponse.getStatusCodeValue());

        ResponseEntity<List<InvoiceByUser>> noContentResponse = backOfficeWebController.getInvoicesByDates(token, 2, validFrom, validTo);
        assertEquals(SC_NO_CONTENT, noContentResponse.getStatusCodeValue());
    }
}
