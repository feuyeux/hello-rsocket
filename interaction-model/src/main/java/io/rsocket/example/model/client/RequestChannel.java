package io.rsocket.example.model.client;

import java.time.Duration;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import static io.rsocket.example.model.server.Server.HOST;
import static io.rsocket.example.model.server.Server.PORT;

@Slf4j
public class RequestChannel {

    public static void main(String[] args) {
        final RSocket rSocket = RSocketFactory.connect()
            .transport(TcpClientTransport.create(HOST, PORT))
            .start()
            .block();

        final Payload blockedResponse = rSocket.requestChannel(
            Flux
                .interval(Duration.ofMillis(5000))
                .map(time -> {
                    log.info("time={}", time);
                    return DefaultPayload.create("Jenny");
                })
        ).blockLast();

        log.info("blockedResponse={}", blockedResponse.getDataUtf8());
    }
}
