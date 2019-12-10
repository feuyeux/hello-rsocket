package org.feuyeux.rsocket.cli;

import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

/**
 * @author feuyeux@gmail.com
 */
@Slf4j
@SpringBootApplication
public class RequesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequesterApplication.class);
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies strategies) {
        return RSocketRequester.builder()
            .rsocketStrategies(strategies)
            .connect(TcpClientTransport.create(7000))
            .block();
    }
}

