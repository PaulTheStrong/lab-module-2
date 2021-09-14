package com.epam.esm.controller;

import com.epam.esm.exception.HttpErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public HttpErrorMessage resourceNotFound(ResourceNotFoundException e) {
        int id = e.getId();
        return new HttpErrorMessage(40401, "Resource (id = " + id + ") not found");
    }




}
