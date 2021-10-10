package com.ncs.nusiss.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletExistsException extends Exception {
    public WalletExistsException() {
        this("Wallet for the user already exist, not allowed to create a new wallet");
    }
    public WalletExistsException(String message) {
        super(message);
    }

}