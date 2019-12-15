package io.rsocket.example.spring;

import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

@Slf4j
@SpringBootApplication
public class RSocketRequesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RSocketRequesterApplication.class);
    }

    /*@Bean
    public RSocket rSocket() {
        return RSocketFactory
            .connect()
            .frameDecoder(PayloadDecoder.ZERO_COPY)
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
            .transport(TcpClientTransport.create(7000))
            .start()
            .block();
    }*/

    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies strategies) {
        // old api
        // return RSocketRequester.wrap(rSocket, MimeTypeUtils.APPLICATION_JSON, rSocketStrategies);

        return RSocketRequester.builder().rsocketStrategies(strategies)
            .connect(TcpClientTransport.create(7000)).block();
    }
}

