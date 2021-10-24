package com.epam.esm.controller;

import com.epam.esm.exception.ErrorCodeToHttpStatusMapper;
import com.epam.esm.exception.HttpErrorResponse;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.i18n.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static com.epam.esm.exception.ExceptionCodes.CONTENT_MEDIA_TYPE_NOT_SUPPORTED;
import static com.epam.esm.exception.ExceptionCodes.METHOD_NOT_SUPPORTED;
import static com.epam.esm.exception.ExceptionCodes.TYPE_MISMATCH;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Translator messageTranslator;
    private final ErrorCodeToHttpStatusMapper mapper;

    @Autowired
    public ControllerExceptionHandler(Translator messageTranslator, ErrorCodeToHttpStatusMapper mapper) {
        this.messageTranslator = messageTranslator;
        this.mapper = mapper;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<HttpErrorResponse> handleServiceException(ServiceException e) {
        String errorCode = e.getErrorCode();
        Object[] arguments = e.getArguments();
        String localizedMessage = messageTranslator.toLocale(errorCode, arguments);
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(errorCode, localizedMessage);
        HttpStatus status = mapper.errorCodeToStatus(errorCode);
        return new ResponseEntity<>(httpErrorResponse, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public HttpErrorResponse handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().get(0);
        String errorCode = error.getDefaultMessage();
        String localizedMessage = messageTranslator.toLocale(errorCode);
        return new HttpErrorResponse(errorCode, localizedMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public HttpErrorResponse handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().stream().findFirst().get();
        String errorCode = constraintViolation.getMessage();
        String localizedMessage = messageTranslator.toLocale(errorCode);
        return new HttpErrorResponse(errorCode, localizedMessage);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public HttpErrorResponse handleUnsupportedMediaTypeException(HttpMediaTypeNotSupportedException e) {
        String errorCode = CONTENT_MEDIA_TYPE_NOT_SUPPORTED;
        MediaType contentType = e.getContentType();
        String localizedMessage = messageTranslator.toLocale(errorCode, contentType);
        return new HttpErrorResponse(errorCode, localizedMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public HttpErrorResponse handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        String requiredType = e.getRequiredType().getName();
        Object value = e.getValue();
        String resultType = value.getClass().getName();
        String errorCode = TYPE_MISMATCH;
        String localizedMessage = messageTranslator.toLocale(errorCode, requiredType, resultType, value.toString());
        return new HttpErrorResponse(errorCode, localizedMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public HttpErrorResponse handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String method = e.getMethod();
        String errorCode = METHOD_NOT_SUPPORTED;
        String localizedMessage = messageTranslator.toLocale(errorCode, method);
        return new HttpErrorResponse(errorCode, localizedMessage);
    }

}
