package com.userloginregistrationapi.exceptionhandler;

import com.userloginregistrationapi.exceptionhandler.responsebodies.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<WebClientResponseExceptionBody>
    handleWebClientResponseExceptionException(WebClientResponseException exception){

        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        WebClientResponseExceptionBody body =
                new WebClientResponseExceptionBody(exception.getResponseBodyAsString());

        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundExceptionBody>
    handleEntityNotFoundException(EntityNotFoundException exception){

        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        EntityNotFoundExceptionBody body =
                new EntityNotFoundExceptionBody(status, exception);

        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ConstraintViolationExceptionResponseBody>
    handleFieldsValidationException(ConstraintViolationException exception) {

        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        ConstraintViolationExceptionResponseBody body =
                new ConstraintViolationExceptionResponseBody(status, exception);

        return new ResponseEntity<>(body, status);
    }
}
