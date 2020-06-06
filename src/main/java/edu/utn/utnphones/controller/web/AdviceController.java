package edu.utn.utnphones.controller.web;

import edu.utn.utnphones.exceptions.InvalidLoginException;
import edu.utn.utnphones.exceptions.UserNotExistsException;
import edu.utn.utnphones.exceptions.ValidationException;
import edu.utn.utnphones.projections.ErrorResponseProjection;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class AdviceController extends ResponseEntityExceptionHandler {


//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(InvalidLoginException.class)
//    public ErrorResponseProjection handleLoginException(InvalidLoginException exc) {
//        return new ErrorResponseProjection(1, "Invalid login");
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ValidationException.class)
//    public ErrorResponseProjection handleValidationException(ValidationException exc) {
//        return new ErrorResponseProjection(2, exc.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(UserNotExistsException.class)
//    public ErrorResponseProjection handleUserNotExists() {
//        return new ErrorResponseProjection(3, "User not exists");
//    }


}
