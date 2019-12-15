package io.rsocket.example.model.client;

import java.util.concurrent.TimeUnit;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;

import static io.rsocket.example.model.server.Server.HOST;
import static io.rsocket.example.model.server.Server.PORT;

@Slf4j
public class FireAndForget {

    public static void main(String[] args) throws InterruptedException {
        RSocket socket = RSocketFactory.connect()
            .transport(TcpClientTransport.create(HOST, PORT))
            .start()
            .block();

        //fireAndForget
        socket.fireAndForget(DefaultPayload.create("Hello world!"))
            .doOnNext(void0 -> {
                log.info("fireAndForget Response? {}", void0);
            })
            .block();

        TimeUnit.SECONDS.sleep(10);
    }
}
