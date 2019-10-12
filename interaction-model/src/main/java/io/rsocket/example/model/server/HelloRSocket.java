package io.rsocket.example.model.server;

import java.time.Duration;
import java.time.Instant;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class HelloRSocket extends AbstractRSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        log.info("Received 'fire-and-forget' request with payload: [{}]", payload.getDataUtf8());
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        log.info("Received 'request response' request with payload: [{}] ", payload.getDataUtf8());
        return Mono.just(DefaultPayload.create("Hello " + payload.getDataUtf8()));
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        log.info("Received 'request stream' request with payload: [{}] ", payload.getDataUtf8());
        return Flux.interval(Duration.ofMillis(1000))
            .map(time -> {
                log.info("time={}", time);
                return DefaultPayload.create("Hello " + payload.getDataUtf8() + " @ " + Instant.now());
            });
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        // running on main thread without scheduler

        // running on one single thread
        final Scheduler scheduler1 =   Schedulers.newSingle("");

        // running on an elastic thread pool ~ Execturos.newCachedThreadPool()
        final Scheduler scheduler2 =  Schedulers.elastic();

        // running on a parallel fix sized (cpu core as default) thread
        final Scheduler scheduler = Schedulers.parallel();

        return Flux.from(payloads)
            .doOnNext(payload -> {
                log.info("Received payload: [{}]", payload.getDataUtf8());
            })
            .map(payload -> DefaultPayload.create("Hello " + payload.getDataUtf8() + " @ " + Instant.now()))
            .subscribeOn(scheduler);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        log.info("Received 'metadata push' request with metadata: [{}]", payload.getMetadataUtf8());
        return Mono.empty();
    }
}