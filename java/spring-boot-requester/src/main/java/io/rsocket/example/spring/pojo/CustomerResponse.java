package io.rsocket.example.spring.pojo;

import lombok.Getter;
import lombok.ToString;

/**
 * @author 六翁 lu.hl@alibaba-inc.com
 * @date 2019/10/09
 */
@Getter
@ToString
public class CustomerResponse {

    private String id;

    private String name;

    public CustomerResponse() {
    }

    public CustomerResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
