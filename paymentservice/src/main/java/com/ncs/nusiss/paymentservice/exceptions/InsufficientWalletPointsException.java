package com.ncs.nusiss.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InsufficientWalletPointsException extends Exception {
    public InsufficientWalletPointsException() {
        this("insufficient points in wallet to purchase the chapter");
    }
    public InsufficientWalletPointsException(String message) {
        super(message);
    }

}