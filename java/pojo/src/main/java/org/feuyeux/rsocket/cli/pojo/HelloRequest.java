package org.feuyeux.rsocket.cli.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author feuyeux@gmail.com
 */
@Data
@ToString
public class HelloRequest {
    private String id;

    public HelloRequest() {
    }

    public HelloRequest(String id) {
        this.id = id;
    }
}
