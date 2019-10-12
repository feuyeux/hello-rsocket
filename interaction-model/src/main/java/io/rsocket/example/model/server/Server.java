package io.rsocket.example.model.server;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Server {
    public static final String HOST = "localhost";
    public static final int PORT = 7000;

    public static void main(String[] args) throws InterruptedException {
        RSocketFactory.receive()
            .acceptor(new HelloSocketAcceptor())
            .transport(TcpServerTransport.create(HOST, PORT))
            .start()
            .subscribe();

        log.info("Server running");

        Thread.currentThread().join();
    }

    @Slf4j
    static class HelloSocketAcceptor implements SocketAcceptor {
        @Override
        public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
            log.info("Received connection with setup payload: [{}] and meta-data: [{}]",
                setup.getDataUtf8(), setup.getMetadataUtf8());

            return Mono.just(new HelloRSocket());
        }
    }
}
