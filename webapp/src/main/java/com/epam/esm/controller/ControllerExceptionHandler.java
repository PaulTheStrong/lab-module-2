package com.epam.esm.controller;

import com.epam.esm.i18n.Translator;
import com.epam.esm.exception.HttpErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.epam.esm.i18n.ExceptionCodes.RESOURCE_NOT_FOUND;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Translator messageTranslator;

    @Autowired
    public ControllerExceptionHandler(Translator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public HttpErrorMessage resourceNotFound(ResourceNotFoundException e) {
        int id = e.getId();
        return new HttpErrorMessage(40401, messageTranslator.toLocale(RESOURCE_NOT_FOUND, id));
    }
}
