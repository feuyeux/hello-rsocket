package org.feuyeux.rsocket.cli;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import io.rsocket.Payload;
import lombok.extern.slf4j.Slf4j;
import org.feuyeux.rsocket.cli.pojo.HelloRequest;
import org.feuyeux.rsocket.cli.pojo.HelloRequests;
import org.feuyeux.rsocket.cli.pojo.HelloResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author feuyeux@gmail.com
 */
@Slf4j
@Controller
public class HelloController {

    private final List<String> HELLO_LIST = Arrays.asList("Hello", "Bonjour", "Hola", "こんにちは", "Ciao", "안녕하세요");

    /**
     * TODO METADATA_PUSH
     *
     * @return
     */

    @ConnectMapping("hello-metadata")
    public Mono<Void> metadataPush(Payload payload) {
        log.info("{}", payload);
        return Mono.empty();
    }

    /**
     * REQUEST_FNF
     * -->!
     *
     * @param helloRequest
     * @return
     */
    @MessageMapping("hello-forget")
    public Mono<Void> fireAndForget(HelloRequest helloRequest) {
        log.info("Received 'fire-and-forget' request with payload:{}", helloRequest.getId());
        return Mono.empty();
    }

    /**
     * REQUEST_RESPONSE
     * request --> <-- response
     *
     * @param helloRequest
     * @return
     */
    @MessageMapping("hello")
    Mono<HelloResponse> requestAndResponse(HelloRequest helloRequest) {
        log.info("Received 'request response' request with payload:{}", helloRequest);
        String id = helloRequest.getId();
        return Mono.just(getHello(id));
    }

    /**
     * REQUEST_STREAM
     * request --> <-- <-- stream
     *
     * @param helloRequests
     * @return
     */
    @MessageMapping("hello-stream")
    Flux<HelloResponse> requestStream(HelloRequests helloRequests) {
        log.info("getCustomers multipleCustomersRequest={}", helloRequests);
        List<String> ids = helloRequests.getIds();
        return Flux.fromIterable(ids)
            .delayElements(Duration.ofMillis(500))
            .map(id -> getHello(id));
    }

    /**
     * REQUEST_CHANNEL
     * request channel --> --> <-- --> <--
     *
     * @param requests
     * @return
     */
    @MessageMapping("hello-channel")
    Flux<HelloResponse> requestChannel(Flux<HelloRequest> requests) {
        return Flux.from(requests)
            .doOnNext(message -> log.info("Received 'request stream' request with payload:{}", message))
            .map(message -> {
                String id = message.getId();
                return getHello(id);
            });
    }

    private HelloResponse getHello(String id) {
        int index;
        try {
            index = Integer.valueOf(id);
        } catch (NumberFormatException ignored) {
            index = 0;
        }
        if (index > 5) {
            return new HelloResponse(id, "你好");
        }
        return new HelloResponse(id, HELLO_LIST.get(index));
    }

    private HelloResponse getHello(int index) {
        return new HelloResponse(String.valueOf(index), HELLO_LIST.get(index));
    }
}