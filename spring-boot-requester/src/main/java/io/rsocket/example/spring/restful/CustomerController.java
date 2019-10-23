package io.rsocket.example.spring.restful;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import io.rsocket.example.spring.CustomerServiceAdapter;
import io.rsocket.example.spring.pojo.CustomerRequest;
import io.rsocket.example.spring.pojo.CustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
public class CustomerController {

    private final CustomerServiceAdapter customerServiceAdapter;

    CustomerController(CustomerServiceAdapter customerServiceAdapter) {
        this.customerServiceAdapter = customerServiceAdapter;
    }

    @GetMapping("/customers/{id}")
    Mono<CustomerResponse> getCustomer(@PathVariable String id) {
        return customerServiceAdapter.getCustomer(id);
    }

    @GetMapping(value = "/customers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<CustomerResponse> getCustomers() {
        return customerServiceAdapter.getCustomers(getRandomIds(10));
    }

    @GetMapping(value = "/customers-channel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<CustomerResponse> getCustomersChannel() {
        return customerServiceAdapter.getCustomerChannel(Flux.interval(Duration.ofMillis(1000))
            .map(id -> new CustomerRequest(UUID.randomUUID().toString())));
    }

    private List<String> getRandomIds(int amount) {
        return IntStream.range(0, amount)
            .mapToObj(n -> UUID.randomUUID().toString())
            .collect(toList());
    }
}
