package io.rsocket.example.model.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;

import static io.rsocket.example.model.server.Server.HOST;
import static io.rsocket.example.model.server.Server.PORT;

@Slf4j
public class RequestResponse {

    public static void main(String[] args) {
        RSocket socket = RSocketFactory.connect()
            .transport(TcpClientTransport.create(HOST, PORT))
            .start()
            .block();

        final Payload requestPayload = DefaultPayload.create("Jenny");

        //requestResponse
        final Payload blockedResponse = socket.requestResponse(requestPayload)
            .doOnNext(payload -> {
                log.info("Received response payload:[{}] metadata:[{}]",
                    payload.getDataUtf8(),
                    payload.getMetadataUtf8());
            })
            .block();

        log.info("blockedResponse={}", blockedResponse.getDataUtf8());
        socket.dispose();
    }
}
