package com.ncs.nusiss.paymentservice.purchase;

import com.ncs.nusiss.paymentservice.entity.ChapterPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterPurchaseRepository extends MongoRepository<ChapterPurchase, String> {
    Optional<ChapterPurchase> findByChapterIdAndUserId(String chapterId, String userId);
}
