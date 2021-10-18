package com.ncs.nusiss.paymentservice.wallet;

import com.ncs.nusiss.paymentservice.entity.Wallet;

import com.ncs.nusiss.paymentservice.exceptions.InsufficientWalletPointsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletExistsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private WalletRepository walletRepository;

    public Wallet createWallet(String userId) throws IllegalArgumentException, WalletExistsException {
        if (userId != null) {
            Optional<Wallet> optionalWallet = walletRepository.findWalletByUserId(userId);
            if (!optionalWallet.isPresent()) {
                Wallet wallet = new Wallet();
                wallet.setUserId(userId);
                wallet.setCurrentPoints(0);
                Wallet savedWallet = walletRepository.insert(wallet);
                return savedWallet;
            } else
                throw new WalletExistsException();
        } else
            throw new IllegalArgumentException();
    }

    public Wallet getWalletByUserId(String userId) throws IllegalStateException, WalletNotFoundException {
        Wallet wallet = new Wallet();
        Optional<Wallet> optionalWallet = walletRepository.findWalletByUserId(userId);
        if (optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            return wallet;
        } else
            throw new WalletNotFoundException();

    }

    public Wallet updateWalletPoints(String walletId, Integer points, Boolean plusPoints) throws WalletNotFoundException, InsufficientWalletPointsException {
        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        if (optionalWallet.isPresent()) {
            Wallet wallet = optionalWallet.get();
            if (plusPoints)
                wallet.setCurrentPoints(wallet.getCurrentPoints() + points);
            else {
                if (wallet.getCurrentPoints() - points < 0)
                    throw new InsufficientWalletPointsException();
                wallet.setCurrentPoints(wallet.getCurrentPoints() - points);
            }
            Wallet savedWallet = walletRepository.save(wallet);
            return savedWallet;
        } else
            throw new WalletNotFoundException();
    }
}

