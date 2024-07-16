package com.example.geocoder.exceptions;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException(){
        super("You have entered an invalid address. Please try a different one.");
    }
}
