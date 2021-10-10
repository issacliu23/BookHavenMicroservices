package com.ncs.nusiss.paymentservice.wallet;

import com.ncs.nusiss.paymentservice.entity.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {

    @Query("{ 'userId' : ?0 }")
    List<Wallet> findWalletByUserId(String userId);
}
