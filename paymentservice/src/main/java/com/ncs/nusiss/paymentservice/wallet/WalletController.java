package com.ncs.nusiss.paymentservice.wallet;

import com.ncs.nusiss.paymentservice.entity.Wallet;
import com.ncs.nusiss.paymentservice.exceptions.WalletExistsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createWallet(@PathVariable(value = "userId") String userId) {
        try {
            Wallet createdWallet = walletService.createWallet(userId);
            if (createdWallet != null)
                return new ResponseEntity<>(createdWallet, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (WalletExistsException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getWallet(@PathVariable(value = "userId") String userId) {
        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            if (wallet != null)
                return new ResponseEntity<>(wallet, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (WalletNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
