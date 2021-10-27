package com.ncs.nusiss.userservice.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainUrlConfiguration {
    @Value("${domain.bookservice.url}")
    public String bookServiceUrl;

    @Value("${domain.paymentservice.url}")
    public String paymentServiceUrl;
}
