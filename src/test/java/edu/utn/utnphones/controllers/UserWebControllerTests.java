package edu.utn.utnphones.controllers;

import edu.utn.utnphones.controller.web.WebUserController;
import edu.utn.utnphones.exceptions.InvalidRequestException;
import edu.utn.utnphones.exceptions.RecordAlreadyExistsException;
import edu.utn.utnphones.exceptions.RecordNotExistsException;
import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.projections.InvoiceByUser;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.service.InvoiceService;
import edu.utn.utnphones.service.UserService;
import edu.utn.utnphones.session.SessionManager;
import edu.utn.utnphones.utils.RestUtils;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.utnphones.model.User.Status.active;
import static edu.utn.utnphones.model.User.Type.client;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserWebControllerTests {

    private WebUserController userWebController;

    private CallService callService;
    private InvoiceService invoiceService;
    private UserService userService;
    private SessionManager sessionManager;
    private RestUtils rest;
    private final String token = "token";
    private User activeUser = new User(1, "Test", "TestLastName", "12312312",
            "test@gmail.com", "1234", null, client, active);
    private User userIdWithNoCalls = new User(2, "", "", "", "", "", null, null, null);
    private User invalidUser = new User(-1, "", "", "", "", "", null, null, null);
    private final String fromValid = "01/01/2020";
    private final String toValid = "05/01/2020";
    private Date fromValidDate;
    private Date toValidDate;
    private Date fromInvalidDate;
    private Date toInvalidDate;
    private final String fromInvalid = "07/01/2020";
    private final String toInvalid = "10/01/2020";
    private CallsByDates call;
    private InvoiceByUser invoice;
    private List<MostCalledProjection> mostCalled = new ArrayList<>();
    private List<MostCalledProjection> emptyMostCalledList = new ArrayList<>();
    private List<CallsByDates> callsProjection = new ArrayList<>();
    private List<CallsByDates> emptyCallsProjectionList = new ArrayList<>();
    private List<InvoiceByUser> invoices = new ArrayList<>();
    private List<InvoiceByUser> emptyInvoicesList = new ArrayList<>();
    private List<Call> callsList = new ArrayList<>();
    private List<Call> emptyCallsList = new ArrayList<>();
    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private MostCalledProjection userWithCalls;
    private MostCalledProjection userWithNoCalls;


    @SneakyThrows
    @Before
    public void initMocks() throws ParseException {
        callService = mock(CallService.class);
        invoiceService = mock(InvoiceService.class);
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);
        rest = mock(RestUtils.class);

        call = factory.createProjection(CallsByDates.class);
        invoice = factory.createProjection(InvoiceByUser.class);
        callsProjection.add(call);
        invoices.add(invoice);

        userWithCalls = factory.createProjection(MostCalledProjection.class);
        userWithCalls.setDestination("Mar del plata");
        userWithCalls.setNumberPhone("223123123");

        userWithNoCalls = factory.createProjection(MostCalledProjection.class);
        userWithNoCalls.setNumberPhone(null);
        userWithNoCalls.setDestination(null);

        fromValidDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromValid);
        toValidDate = new SimpleDateFormat("dd/MM/yyyy").parse(toValid);
        fromInvalidDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromInvalid);
        toInvalidDate = new SimpleDateFormat("dd/MM/yyyy").parse(toInvalid);

        userWebController = new WebUserController(callService, invoiceService, userService, sessionManager, rest);

        mostCalled.add(userWithCalls);
        callsList.add(new Call());

        when(sessionManager.getCurrentUser(token)).thenReturn(activeUser);
        when(sessionManager.getCurrentUser("2")).thenReturn(userIdWithNoCalls);
        when(sessionManager.getCurrentUser("-1")).thenReturn(invalidUser);
        when(callService.getCallsByDates(1, fromValidDate, toValidDate)).thenReturn(callsProjection);
        when(callService.getCallsByDates(1, fromInvalidDate, toInvalidDate)).thenReturn(emptyCallsProjectionList);
        when(callService.getCallsByUser(1)).thenReturn(callsList);
        when(callService.getCallsByUser(2)).thenReturn(emptyCallsList);
        when(invoiceService.getInvoicesByDates(1, fromValidDate, toValidDate)).thenReturn(invoices);
        when(invoiceService.getInvoicesByDates(1, fromInvalidDate, toInvalidDate)).thenReturn(emptyInvoicesList);
        when(invoiceService.getInvoicesByUser(1)).thenReturn(invoices);
        when(invoiceService.getInvoicesByUser(2)).thenReturn(emptyInvoicesList);
        when(userService.getMostCalledFromUser(1, 1)).thenReturn(mostCalled);
        when(userService.getMostCalledFromUser(2, 1)).thenReturn(emptyMostCalledList);
        when(userService.getMostCalledFromUser(-1, 1)).thenReturn(null);
        when(rest.getLocation(activeUser)).thenReturn(new URI("http://localhost:8080/test"));

    }

    @Test
    public void updateUserOk() throws RecordAlreadyExistsException, RecordNotExistsException {
        ResponseEntity<URI> uriResponseEntity = userWebController.updateUser(token, null);
        assertEquals(SC_CREATED, uriResponseEntity.getStatusCodeValue());
    }
    @Test
    public void getCalls() throws ParseException, InvalidRequestException {
        ResponseEntity<List<Call>> callsByDates = userWebController.getCallsByUser(token);
        assertEquals(SC_OK, callsByDates.getStatusCodeValue());
        assertEquals(1, callsByDates.getBody().size());
    }

    @Test
    public void getCallsNoContent() throws ParseException, InvalidRequestException {
        ResponseEntity<List<Call>> callsByDates = userWebController.getCallsByUser("2");
        assertEquals(SC_NO_CONTENT, callsByDates.getStatusCodeValue());
    }


    @Test
    public void getCallsByDatesOk() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> callsByDates = userWebController.getCallsByDates(token, fromValid, toValid);
        assertEquals(SC_OK, callsByDates.getStatusCodeValue());
        assertEquals(1, callsByDates.getBody().size());
    }

    @Test
    public void getCallsByDatesNoContent() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> callsByDates = userWebController.getCallsByDates(token, fromInvalid, toInvalid);
        assertEquals(SC_NO_CONTENT, callsByDates.getStatusCodeValue());
    }

    @Test
    public void getInvoices() throws InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> callsByDates = userWebController.getInvoices(token);
        assertEquals(SC_OK, callsByDates.getStatusCodeValue());
        assertEquals(1, callsByDates.getBody().size());
    }

    @Test
    public void getInvoicesNoContent() throws InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> callsByDates = userWebController.getInvoices("2");
        assertEquals(SC_NO_CONTENT, callsByDates.getStatusCodeValue());
    }

    @Test
    public void getInvoicesByDatesOk() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, fromValid, toValid);
        assertEquals(SC_OK, invoicesByDates.getStatusCodeValue());
        assertEquals(1, invoicesByDates.getBody().size());
    }

    @Test(expected = InvalidRequestException.class)
    public void getCallsByDateParsingError() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> invoicesByDates = userWebController.getCallsByDates(token, "10/10", "20/10/99");
    }

    @Test(expected = InvalidRequestException.class)
    public void getCallsByDateInvalidRequest() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> invoicesByDates = userWebController.getCallsByDates(token, null, null);
    }

    @Test(expected = InvalidRequestException.class)
    public void getCallsByInvalidRequestNull1() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> invoicesByDates = userWebController.getCallsByDates(token, "10/10", null);
    }

    @Test(expected = InvalidRequestException.class)
    public void getCallsByInvalidRequestNull2() throws ParseException, InvalidRequestException {
        ResponseEntity<List<CallsByDates>> invoicesByDates = userWebController.getCallsByDates(token, null, "10/10");
    }

    @Test(expected = InvalidRequestException.class)
    public void getInvoicesParsingError() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, "10/10", "20/10/99");
    }

    @Test(expected = InvalidRequestException.class)
    public void getInvoicesInvalidRequest() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, null, null);
    }

    @Test(expected = InvalidRequestException.class)
    public void getInvoicesInvalidRequestnull1() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, null, "10/10");
    }

    @Test(expected = InvalidRequestException.class)
    public void getInvoicesInvalidRequestnull2() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, "10/10", null);
    }

    @Test
    public void getInvoicesByDatesNoContent() throws ParseException, InvalidRequestException {
        ResponseEntity<List<InvoiceByUser>> invoicesByDates = userWebController.getInvoicesByDates(token, fromInvalid, toInvalid);
        assertEquals(SC_NO_CONTENT, invoicesByDates.getStatusCodeValue());
    }

    @Test
    public void mostCalledOk() throws InvalidRequestException, RecordNotExistsException {
        ResponseEntity<List<MostCalledProjection>> mostCalledFromUser = userWebController.getMostCalledFromUser(token, null);
        assertEquals(SC_OK, mostCalledFromUser.getStatusCodeValue());
    }

    @Test(expected = InvalidRequestException.class)
    public void mostCalledInvalidRequest() throws InvalidRequestException, RecordNotExistsException {
        ResponseEntity<List<MostCalledProjection>> mostCalledFromUser = userWebController.getMostCalledFromUser(token, 0);
        assertEquals(SC_OK, mostCalledFromUser.getStatusCodeValue());
    }

    @Test
    public void mostCalledNoContent() throws InvalidRequestException, RecordNotExistsException {
        ResponseEntity<List<MostCalledProjection>> mostCalledFromUser = userWebController.getMostCalledFromUser("2", 1);
        assertEquals(SC_NO_CONTENT, mostCalledFromUser.getStatusCodeValue());
    }

}
