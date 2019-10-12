package io.rsocket.example.transfer;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public class WebsocketFireForgetExample {
    public static void main(String[] args) throws Exception {
        final int port = 7777;

        RSocket responseHandler = new AbstractRSocket() {
            @Override
            public Mono<Void> fireAndForget(Payload payload) {
                System.out.printf("fire-forget: %s%n", payload.getDataUtf8());
                return Mono.empty();
            }
        };

        Disposable server = RSocketFactory.receive()
            .acceptor(
                (setupPayload, rsocket) ->
                    Mono.just(responseHandler))
            .transport(WebsocketServerTransport.create("localhost", port))
            .start()
            .subscribe();

        System.out.printf("websocket server started on port %d%n", port);

        RSocket socket =
            RSocketFactory.connect()
                .transport(WebsocketClientTransport.create("localhost", port))
                .start()
                .block();

        System.out.printf("websocket client initialized, connecting to port %d%n", port);

        socket
            .fireAndForget(DefaultPayload.create("message send as fire and forget"))
            .doOnNext(System.out::println)
            .block();

        Thread.sleep(1_000);
        socket.dispose();
        server.dispose();
    }
}
