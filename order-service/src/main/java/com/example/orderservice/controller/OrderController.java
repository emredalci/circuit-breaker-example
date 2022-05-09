package com.example.orderservice.controller;

import com.example.orderservice.model.Payment;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    public static final String CIRCUIT_BREAKER_INSTANCE_NAME = "order";
    public static final String FALLBACK_METHOD = "fallback";

    @GetMapping(value = "/{delay}")
    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    public List<Payment> getPaymentWithDelay(@PathVariable int delay) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(delay);
        return buildPaymentList();
    }

    private List<Payment> buildPaymentList() {
        Payment payment1 = Payment.builder()
            .id(1)
            .productName("productName1")
            .build();
        Payment payment2 = Payment.builder()
            .id(2)
            .productName("productName2")
            .build();
        return List.of(payment1, payment2);
    }

    private List<Payment> fallback(Exception e) {
        log.error("There is a problem at payment service. This is a default message");
        return Collections.emptyList();
    }

}
