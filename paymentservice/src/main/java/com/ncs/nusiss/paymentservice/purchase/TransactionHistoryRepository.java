package com.ncs.nusiss.paymentservice.purchase;

import com.ncs.nusiss.paymentservice.entity.TransactionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory, String> {
}
