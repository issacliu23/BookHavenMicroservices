package com.ncs.nusiss.userservice.entity;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

public class Wallet {
    private String walletId;
    private String userId;
    private Integer currentPoints;
}
