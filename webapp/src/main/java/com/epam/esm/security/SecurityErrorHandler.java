package com.epam.esm.security;

import com.epam.esm.exception.ErrorCodeToHttpStatusMapper;
import com.epam.esm.exception.HttpErrorResponse;
import com.epam.esm.i18n.Translator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.exception.ExceptionCodes.ACCESS_DENIED;
import static com.epam.esm.exception.ExceptionCodes.INVALID_USERNAME_OR_PASSWORD;
import static com.epam.esm.exception.ExceptionCodes.NOT_ENOUGH_RIGHTS;

@Component
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationFailureHandler, AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    @Autowired
    private Translator messageTranslator;
    @Autowired
    private ErrorCodeToHttpStatusMapper mapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        sendError(INVALID_USERNAME_OR_PASSWORD, response);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        sendError(ACCESS_DENIED, response);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendError(NOT_ENOUGH_RIGHTS, response);
    }

    public void sendError(String errorCode, HttpServletResponse response) throws IOException {
        String localizedMessage = messageTranslator.toLocale(errorCode);
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse(errorCode, localizedMessage);
        HttpStatus status = mapper.errorCodeToStatus(errorCode);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
        String errorResponseString = objectMapper.writeValueAsString(httpErrorResponse);
        response.getWriter().print(errorResponseString);
    }

}
