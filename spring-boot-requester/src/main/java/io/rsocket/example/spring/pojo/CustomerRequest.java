package io.rsocket.example.spring.pojo;

import lombok.Getter;
import lombok.ToString;

/**
 * @author 六翁 lu.hl@alibaba-inc.com
 * @date 2019/10/09
 */
@Getter
@ToString
public class CustomerRequest {
    private String id;

    public CustomerRequest() {
    }

    public CustomerRequest(String id) {
        this.id = id;
    }
}
