package com.ncs.nusiss.userservice.events;

import com.ncs.nusiss.userservice.entity.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class UserDomainEvents {

    private final Logger logger = LoggerFactory.getLogger(UserDomainEvents.class);

    @Autowired
    private DomainUrlConfiguration url;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Wallet userCreated(String username) {
        return webClientBuilder.build()
                .post()
                .uri(url.paymentServiceUrl+"/wallet/"+username)
                .retrieve()
                .bodyToMono(Wallet.class)
                .doOnError(error -> logger.error("An error has occurred {}", error.getMessage()))
                .onErrorResume(error -> Mono.just(new Wallet()))
                .block();
    }
}
