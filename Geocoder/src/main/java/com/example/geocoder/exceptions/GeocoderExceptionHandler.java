package com.example.geocoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.geocoder.responses.ErrorResponse;

@ControllerAdvice   
public class GeocoderExceptionHandler {
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(){
        return new ResponseEntity<>(new ErrorResponse("This is a generic Runtime Exception Placeholder. FIX THIS!"), HttpStatus.NOT_IMPLEMENTED);
    }
    
}
