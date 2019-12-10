package org.feuyeux.rsocket.cli.pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.feuyeux.async.redis.LettuceCoon;
import org.feuyeux.async.redis.RedisConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class TestSerializable {
    private LettuceCoon lettuceCoon;

    @Before
    public void before() {
        RedisConfig redisProps = new RedisConfig();
        redisProps.setHost("rm-tatuds.redis.rdstest.tbsite.net");
        redisProps.setPwd("kvStore17");
        redisProps.setPort(6379);
        redisProps.setConnectionTimeout(1000);
        lettuceCoon = new LettuceCoon(redisProps);
        lettuceCoon.init();
    }

    @After
    public void after() {
        if (lettuceCoon != null) {
            lettuceCoon.destroy();
        }
    }

    @Test
    public void test() {
        String json = lettuceCoon.readString("2019-12-09-RSOCKET");
        log.info("{}", json);
        HelloResponse helloResponse = JSON.parseObject(json, HelloResponse.class);
        log.info("{}", helloResponse);
    }
}
