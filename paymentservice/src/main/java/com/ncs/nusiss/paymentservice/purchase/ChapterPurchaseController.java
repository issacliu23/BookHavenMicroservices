package com.ncs.nusiss.paymentservice.purchase;


import com.ncs.nusiss.paymentservice.entity.Chapter;
import com.ncs.nusiss.paymentservice.entity.ChapterPurchase;
import com.ncs.nusiss.paymentservice.exceptions.ChapterPurchasedException;
import com.ncs.nusiss.paymentservice.exceptions.InsufficientWalletPointsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import com.ncs.nusiss.paymentservice.securityconfig.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api")
public class ChapterPurchaseController {

    @Autowired
    ChapterPurchaseService chapterPurchaseService;

    @PostMapping("/chapterPurchase")
    public ResponseEntity<?> purchaseChapter(@RequestBody Chapter chapter, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String username = JwtUtils.getUsernameFromJwt();
            if(chapter == null || username.isEmpty() || username.equalsIgnoreCase("anonymoususer")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            ChapterPurchase chapterPurchase = chapterPurchaseService.purchaseChapter(chapter.getChapterId(), username, authorizationHeader);
            return new ResponseEntity<>(chapterPurchase, HttpStatus.OK);
        } catch (IllegalArgumentException | ChapterPurchasedException | WalletNotFoundException | InsufficientWalletPointsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
