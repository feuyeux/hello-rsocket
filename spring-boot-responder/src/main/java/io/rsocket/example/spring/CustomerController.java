package io.rsocket.example.spring;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.rsocket.example.spring.pojo.CustomerRequest;
import io.rsocket.example.spring.pojo.CustomerResponse;
import io.rsocket.example.spring.pojo.MultipleCustomersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class CustomerController {

    private final List<String> RANDOM_NAMES = Arrays.asList("Andrew", "Joe", "Matt", "Rachel", "Robin", "Jack");

    @MessageMapping("customer")
    CustomerResponse getCustomer(CustomerRequest customerRequest) {
        log.info("getCustomer customerRequest={}", customerRequest);
        return new CustomerResponse(customerRequest.getId(), getRandomName());
    }

    @MessageMapping("customer-stream")
    Flux<CustomerResponse> getCustomers(MultipleCustomersRequest multipleCustomersRequest) {
        log.info("getCustomers multipleCustomersRequest={}", multipleCustomersRequest);
        return Flux.range(0, multipleCustomersRequest.getIds().size())
            .delayElements(Duration.ofMillis(500))
            .map(i -> new CustomerResponse(multipleCustomersRequest.getIds().get(i), getRandomName()));
    }

    @MessageMapping("customer-channel")
    Flux<CustomerResponse> getCustomersChannel(Flux<CustomerRequest> requests) {
        log.info("getCustomersChannel requests={}");
        return Flux.from(requests)
            .doOnNext(message -> log.info("Received 'customerChannel' request [{}]", message))
            .map(message -> new CustomerResponse(message.getId(), getRandomName()));
    }

    private String getRandomName() {
        return RANDOM_NAMES.get(new Random().nextInt(RANDOM_NAMES.size() - 1));
    }
}