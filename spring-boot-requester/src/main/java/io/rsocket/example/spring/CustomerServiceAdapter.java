package io.rsocket.example.spring;

import java.util.List;

import io.rsocket.example.spring.pojo.CustomerRequest;
import io.rsocket.example.spring.pojo.CustomerResponse;
import io.rsocket.example.spring.pojo.MultipleCustomersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomerServiceAdapter {
    private final RSocketRequester rSocketRequester;

    public CustomerServiceAdapter(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    public Mono<CustomerResponse> getCustomer(String id) {
        return rSocketRequester
            .route("customer")
            .data(new CustomerRequest(id))
            .retrieveMono(CustomerResponse.class)
            .doOnNext(customerResponse -> {
                log.info("Received customer as mono [{}]", customerResponse);
            });
    }

    public Flux<CustomerResponse> getCustomers(List<String> ids) {
        return rSocketRequester
            .route("customer-stream")
            .data(new MultipleCustomersRequest(ids))
            .retrieveFlux(CustomerResponse.class)
            .doOnNext(customerResponse -> {
                log.info("Received customer as flux [{}]", customerResponse);
            });
    }

    public Flux<CustomerResponse> getCustomerChannel(Flux<CustomerRequest> customerRequestFlux) {
        return rSocketRequester
            .route("customer-channel")
            .data(customerRequestFlux, CustomerRequest.class)
            .retrieveFlux(CustomerResponse.class)
            .doOnNext(customerResponse -> {
                    log.info("Received customer as flux [{}]", customerResponse);
                }
            );
    }
}
