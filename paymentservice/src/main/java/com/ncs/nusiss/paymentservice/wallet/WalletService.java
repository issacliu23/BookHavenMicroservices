package com.ncs.nusiss.paymentservice.wallet;

import com.ncs.nusiss.paymentservice.entity.Wallet;

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
            if(userId!= null ) {
                Optional<Wallet> optionalWallet = walletRepository.findById(userId);
                if (!optionalWallet.isPresent()) {
                    Wallet wallet = new Wallet();
                    wallet.setUserId(userId);
                    Wallet savedWallet = walletRepository.insert(wallet);
                    return savedWallet;
                } else
                throw new WalletExistsException();
            }
            else
                throw new IllegalArgumentException();
    }

    public Wallet getWalletByUserId(String userId) throws IllegalStateException, WalletNotFoundException {
        Wallet wallet = new Wallet();
        List<Wallet> walletList = walletRepository.findWalletByUserId(userId);
        if (!walletList.isEmpty()) {
            wallet = walletList.get(0);
        } else
            throw new WalletNotFoundException();
        return wallet;
    }
}
