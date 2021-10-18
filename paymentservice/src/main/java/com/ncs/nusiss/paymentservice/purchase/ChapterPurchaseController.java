package com.ncs.nusiss.paymentservice.purchase;


import com.ncs.nusiss.paymentservice.entity.ChapterPurchase;
import com.ncs.nusiss.paymentservice.exceptions.ChapterPurchasedException;
import com.ncs.nusiss.paymentservice.exceptions.InsufficientWalletPointsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ChapterPurchaseController {

    @Autowired
    ChapterPurchaseService chapterPurchaseService;

    @PostMapping("/chapterPurchase/{chapterId}/{userId}")
    public ResponseEntity<?> purchaseChapter(@PathVariable(value = "chapterId") String chapterId, @PathVariable(value = "userId") String userId) {
        try {
            if (chapterId != null && userId != null) {
                ChapterPurchase chapterPurchase = chapterPurchaseService.purchaseChapter(chapterId, userId);
                return new ResponseEntity<>(chapterPurchase, HttpStatus.OK);
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException | ChapterPurchasedException | WalletNotFoundException | InsufficientWalletPointsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
