package io.rsocket.example.spring.pojo;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * @author 六翁 lu.hl@alibaba-inc.com
 * @date 2019/10/09
 */
@Getter
@ToString
public class MultipleCustomersRequest {
    private List<String> ids;

    public MultipleCustomersRequest() {
    }

    public MultipleCustomersRequest(List<String> ids) {
        this.ids = ids;
    }
}
