package com.epam.esm.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.epam.esm.security.SecurityErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.esm.exception.ExceptionCodes.ACCESS_DENIED;
import static com.epam.esm.exception.ExceptionCodes.INVALID_TOKEN;
import static com.epam.esm.exception.ExceptionCodes.TOKEN_EXPIRED;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityErrorHandler handler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            handler.sendError(TOKEN_EXPIRED, response);
        } catch (JWTVerificationException e) {
            handler.sendError(INVALID_TOKEN, response);
        } catch (FilterException e) {
            handler.sendError(ACCESS_DENIED, response);
        }
    }
}
