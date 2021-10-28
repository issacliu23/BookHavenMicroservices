package com.ncs.nusiss.paymentservice.wallet;

import com.ncs.nusiss.paymentservice.entity.Wallet;
import com.ncs.nusiss.paymentservice.exceptions.InsufficientWalletPointsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletExistsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import com.ncs.nusiss.paymentservice.securityconfig.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createWallet(@PathVariable(value = "userId") String userId) {
        System.out.print("Creating wallet for: " + userId);
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

    @GetMapping()
    public ResponseEntity<?> getWallet() {
        try {
            Wallet wallet = walletService.getWalletByUserId(JwtUtils.getUsernameFromJwt());

            if (wallet != null)
                return new ResponseEntity<>(wallet, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (WalletNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/topUp")
    public ResponseEntity<?> topUpWallet(@RequestBody String points) {
        int amount = 0;
        try {
            amount = Integer.parseInt(points);
        }
        catch(NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        if(amount > 0) {
            try {
                Wallet wallet = walletService.updateWalletPointsWithUserId(JwtUtils.getUsernameFromJwt(), amount, true);
                if (wallet != null)
                    return new ResponseEntity<>(wallet, HttpStatus.OK);
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (WalletNotFoundException | IllegalArgumentException | InsufficientWalletPointsException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawWallet(@RequestBody String points) {
        int amount = 0;
        try {
            amount = Integer.parseInt(points);
        }
        catch(NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        if(amount > 0) {
            try {
                Wallet wallet = walletService.updateWalletPointsWithUserId(JwtUtils.getUsernameFromJwt(), amount, false);
                if (wallet != null)
                    return new ResponseEntity<>(wallet, HttpStatus.OK);
                else
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (WalletNotFoundException | IllegalArgumentException | InsufficientWalletPointsException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
