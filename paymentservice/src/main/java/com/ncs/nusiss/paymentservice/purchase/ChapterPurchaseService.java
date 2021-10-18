package com.ncs.nusiss.paymentservice.purchase;

import com.ncs.nusiss.paymentservice.entity.*;
import com.ncs.nusiss.paymentservice.enums.TransactionType;
import com.ncs.nusiss.paymentservice.exceptions.ChapterPurchasedException;
import com.ncs.nusiss.paymentservice.exceptions.InsufficientWalletPointsException;
import com.ncs.nusiss.paymentservice.exceptions.WalletNotFoundException;
import com.ncs.nusiss.paymentservice.wallet.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ChapterPurchaseService {

    private final Logger logger = LoggerFactory.getLogger(ChapterPurchaseService.class);

    @Autowired
    private ChapterPurchaseRepository chapterPurchaseRepository;

    @Autowired
    private ChapterPointsTransferRepository chapterPointsTransferRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WalletService walletService;

    public ChapterPurchase purchaseChapter(String chapterId, String userId) throws IllegalArgumentException, ChapterPurchasedException, WalletNotFoundException, InsufficientWalletPointsException {
        if (chapterId != null && userId != null) {
            Optional<ChapterPurchase> optionalPurchase = chapterPurchaseRepository.findByChapterIdAndUserId(chapterId, userId);
            if (!optionalPurchase.isPresent()) {
                Chapter chapter = restTemplate.getForObject("http://localhost:8081/chapter/" + chapterId, Chapter.class);
                Integer pointsRequired = chapter.getPointsRequiredForChapter();

                Wallet userWallet = walletService.getWalletByUserId(userId);
                Wallet savedUserWallet = walletService.updateWalletPoints(userWallet.getWalletId(), pointsRequired, false);

                String authorId = chapter.getAuthorId();
                Wallet authorWallet = walletService.getWalletByUserId(authorId);
                Wallet savedAuthorWallet = walletService.updateWalletPoints(authorWallet.getWalletId(), pointsRequired, true);

                ChapterPointsTransfer chapterPointsTransfer = new ChapterPointsTransfer();
                chapterPointsTransfer.setFromWalletId(userWallet.getWalletId());
                chapterPointsTransfer.setToWalletId(authorWallet.getWalletId());
                chapterPointsTransfer.setPointsTransferred(pointsRequired);
                ChapterPointsTransfer savedChapterPointsTransfer = chapterPointsTransferRepository.insert(chapterPointsTransfer);

                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setTransactionType(TransactionType.PurchaseChapter);
                transactionHistory.setPointsInvolved(pointsRequired);
                transactionHistory.setReferenceId(savedChapterPointsTransfer.getTransferId());
                TransactionHistory savedTransactionHistory = transactionHistoryRepository.insert(transactionHistory);

                ChapterAccess chapterAccess = new ChapterAccess();
                chapterAccess.setChapterId(chapterId);
                chapterAccess.setUserId(userId);
                ChapterAccess savedChapterAccess = restTemplate.postForObject("http://localhost:8081/chapterAccess/" + chapterId + "/" + userId, chapterAccess, ChapterAccess.class);

                ChapterPurchase chapterPurchase = new ChapterPurchase();
                chapterPurchase.setUserId(userId);
                chapterPurchase.setChapterId(chapterId);
                ChapterPurchase savedChapterPurchase = chapterPurchaseRepository.insert(chapterPurchase);
                return savedChapterPurchase;
            } else
                throw new ChapterPurchasedException();
        } else
            throw new IllegalArgumentException();
    }

}
