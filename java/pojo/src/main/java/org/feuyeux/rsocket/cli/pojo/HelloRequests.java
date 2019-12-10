package org.feuyeux.rsocket.cli.pojo;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author feuyeux@gmail.com
 */
@Data
@ToString
public class HelloRequests {
    private List<String> ids;

    public HelloRequests() {
    }

    public HelloRequests(List<String> ids) {
        this.ids = ids;
    }
}
