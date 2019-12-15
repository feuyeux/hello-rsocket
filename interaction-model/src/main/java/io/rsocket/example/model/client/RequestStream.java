package io.rsocket.example.model.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import static io.rsocket.example.model.server.Server.HOST;
import static io.rsocket.example.model.server.Server.PORT;

public class RequestStream {

    public static void main(String[] args) throws InterruptedException {
        RSocket socket = RSocketFactory.connect()
            .transport(TcpClientTransport.create(HOST, PORT))
            .start()
            .block();

        final Payload payload = DefaultPayload.create("Jenny", "example-metadata");

        //requestStream
        final Flux<Payload> payloadFlux = socket.requestStream(payload);

        payloadFlux
            .limitRequest(15)
            .subscribe(new BackPressureSubscriber());

        //socket.dispose();
        Thread.currentThread().join();
    }

    @Slf4j
    private static class BackPressureSubscriber implements Subscriber<Payload> {

        private static final Integer NUMBER_OF_REQUESTS_TO_PROCESS = 5;
        private Subscription subscription;
        int receivedItems;

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            subscription.request(NUMBER_OF_REQUESTS_TO_PROCESS);
        }

        @Override
        public void onNext(Payload payload) {
            receivedItems++;
            if (receivedItems % NUMBER_OF_REQUESTS_TO_PROCESS == 0) {
                log.info("Requesting next [{}] elements", receivedItems);
                subscription.request(NUMBER_OF_REQUESTS_TO_PROCESS);
            }
        }

        @Override
        public void onError(Throwable t) {
            log.error("Stream subscription error", t);
        }

        @Override
        public void onComplete() {
            log.info("Completing subscription");
        }
    }
}
