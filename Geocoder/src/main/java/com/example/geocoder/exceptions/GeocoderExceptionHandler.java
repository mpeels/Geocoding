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

    @ExceptionHandler(MissingZipOrCityStateException.class)
    public ResponseEntity<ErrorResponse> handleMissingZipOrCityStateExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MissingStreetException.class)
    public ResponseEntity<ErrorResponse> handleMissingParametersExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAddressExceptions(Exception e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(){
        return new ResponseEntity<>(new ErrorResponse("This is a placeholder exception. FIX THIS!"), HttpStatus.BAD_REQUEST);
    }
}