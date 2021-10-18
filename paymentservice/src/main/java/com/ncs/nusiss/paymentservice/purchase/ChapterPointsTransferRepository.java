package com.ncs.nusiss.paymentservice.purchase;

import com.ncs.nusiss.paymentservice.entity.ChapterPointsTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterPointsTransferRepository extends MongoRepository<ChapterPointsTransfer, String> {
}
