package com.serbar.wordle.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@EnableWebMvc
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler({WordNotExistException.class, HashNotExistException.class})
    protected ResponseEntity<Object> handleKnownException(Exception exception, WebRequest request) {
        logger.error("Got internal exception", exception);
        return handleExceptionInternal(
                exception,
                exception.getMessage(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception exception, WebRequest request) {
        logger.error("Got unexpected exception", exception);

        return handleExceptionInternal(
                exception,
                "Got unexpected exception",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

}
