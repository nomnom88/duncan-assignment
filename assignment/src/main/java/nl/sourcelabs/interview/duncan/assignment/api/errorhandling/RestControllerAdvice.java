package nl.sourcelabs.interview.duncan.assignment.api.errorhandling;

import javax.persistence.EntityNotFoundException;

import nl.sourcelabs.interview.duncan.assignment.api.model.ErrorResponse;
import nl.sourcelabs.interview.duncan.assignment.util.UserInputValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice {

    @ExceptionHandler({ Throwable.class })
    public ResponseEntity<ErrorResponse> defaultErrorHandler(final Throwable ex) {
        log.error("Unexpected exception caught, reporting internal server error to user", ex);
        return new ResponseEntity<>(ErrorResponse.builder()
                .message("Something has gone wrong in the server - consult server logs")
                .build(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEntityNotFound(final EntityNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), new HttpHeaders(), HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler({ UserInputValidationException.class })
    public ResponseEntity<ErrorResponse> handleBadUserInput(final UserInputValidationException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().message(ex.getMessage()).build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
