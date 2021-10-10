package com.ncs.nusiss.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException() {
        this("Wallet not found");
    }
    public WalletNotFoundException(String message) {
        super(message);
    }

}