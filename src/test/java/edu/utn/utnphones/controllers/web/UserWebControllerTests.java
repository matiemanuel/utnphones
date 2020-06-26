package edu.utn.utnphones.controllers.web;

import edu.utn.utnphones.controller.web.UserWebController;
import edu.utn.utnphones.model.Invoice;
import edu.utn.utnphones.model.User;
import edu.utn.utnphones.projections.CallsByDates;
import edu.utn.utnphones.projections.InvoiceByDates;
import edu.utn.utnphones.projections.MostCalledProjection;
import edu.utn.utnphones.service.CallService;
import edu.utn.utnphones.service.InvoiceService;
import edu.utn.utnphones.service.UserService;
import edu.utn.utnphones.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;

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

    private UserWebController userWebController;

    private CallService callService;
    private InvoiceService invoiceService;
    private UserService userService;
    private SessionManager sessionManager;
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
    private InvoiceByDates invoice;
    private List<CallsByDates> calls = new ArrayList<>();
    private List<CallsByDates> emptyCallsList = new ArrayList<>();
    private List<InvoiceByDates> invoices = new ArrayList<>();
    private List<InvoiceByDates> emptyInvoicesList = new ArrayList<>();
    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private MostCalledProjection userWithCalls;
    private MostCalledProjection userWithNoCalls;


    @Before
    public void initMocks() throws ParseException {
        callService = mock(CallService.class);
        invoiceService = mock(InvoiceService.class);
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);

        call = factory.createProjection(CallsByDates.class);
        invoice = factory.createProjection(InvoiceByDates.class);
        calls.add(call);
        invoices.add(invoice);

        userWithCalls = factory.createProjection(MostCalledProjection.class);
        userWithCalls.setName("Test");
        userWithCalls.setLastname("TestLastName");
        userWithCalls.setMostCalled("123123");

        userWithNoCalls = factory.createProjection(MostCalledProjection.class);
        userWithNoCalls.setName("Test2");
        userWithNoCalls.setLastname("TestLastName2");

        fromValidDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromValid);
        toValidDate = new SimpleDateFormat("dd/MM/yyyy").parse(toValid);
        fromInvalidDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromInvalid);
        toInvalidDate = new SimpleDateFormat("dd/MM/yyyy").parse(toInvalid);

        userWebController = new UserWebController(callService, invoiceService, userService, sessionManager);

        when(sessionManager.getCurrentUser(token)).thenReturn(activeUser);
        when(sessionManager.getCurrentUser("2")).thenReturn(userIdWithNoCalls);
        when(sessionManager.getCurrentUser("-1")).thenReturn(invalidUser);
        when(callService.getCallsByDates(1, fromValidDate, toValidDate)).thenReturn(calls);
        when(callService.getCallsByDates(1, fromInvalidDate, toInvalidDate)).thenReturn(emptyCallsList);
        when(invoiceService.getInvoicesByDates(1, fromValidDate, toValidDate)).thenReturn(invoices);
        when(invoiceService.getInvoicesByDates(1, fromInvalidDate, toInvalidDate)).thenReturn(emptyInvoicesList);
        when(userService.getMostCalledFromUser(1)).thenReturn(userWithCalls);
        when(userService.getMostCalledFromUser(2)).thenReturn(userWithNoCalls);
        when(userService.getMostCalledFromUser(-1)).thenReturn(null);

    }

    @Test
    public void getCallsByDatesOk() throws ParseException {
        ResponseEntity<List<CallsByDates>> callsByDates = userWebController.getCallsByDates(token, fromValid, toValid);
        assertEquals(SC_OK, callsByDates.getStatusCodeValue());
        assertEquals(1, callsByDates.getBody().size());
    }

    @Test
    public void getCallsByDatesNoContent() throws ParseException {
        ResponseEntity<List<CallsByDates>> callsByDates = userWebController.getCallsByDates(token, fromInvalid, toInvalid);
        assertEquals(SC_NO_CONTENT, callsByDates.getStatusCodeValue());
    }

    @Test
    public void getInvoicesByDatesOk() throws ParseException {
        ResponseEntity<List<InvoiceByDates>> invoicesByDates = userWebController.getInvoicesByDates(token, fromValid, toValid);
        assertEquals(SC_OK, invoicesByDates.getStatusCodeValue());
        assertEquals(1, invoicesByDates.getBody().size());
    }

    @Test
    public void getInvoicesByDatesNoContent() throws ParseException {
        ResponseEntity<List<InvoiceByDates>> invoicesByDates = userWebController.getInvoicesByDates(token, fromInvalid, toInvalid);
        assertEquals(SC_NO_CONTENT, invoicesByDates.getStatusCodeValue());
    }

    @Test
    public void mostCalledOk() {
        ResponseEntity<MostCalledProjection> mostCalledFromUser = userWebController.getMostCalledFromUser(token);
        assertEquals(SC_OK, mostCalledFromUser.getStatusCodeValue());
    }

    @Test
    public void mostCalledNoContent() {
        ResponseEntity<MostCalledProjection> mostCalledFromUser = userWebController.getMostCalledFromUser("2");
        assertEquals(SC_NO_CONTENT, mostCalledFromUser.getStatusCodeValue());
    }

    @Test
    public void mostCalledNotFound() {
        ResponseEntity<MostCalledProjection> mostCalledFromUser = userWebController.getMostCalledFromUser("-1");
        assertEquals(SC_NOT_FOUND, mostCalledFromUser.getStatusCodeValue());
    }

}
