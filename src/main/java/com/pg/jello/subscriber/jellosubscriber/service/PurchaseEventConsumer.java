package com.pg.jello.subscriber.jellosubscriber.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pg.jello.subscriber.jellosubscriber.bean.Product;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PurchaseEventConsumer implements StreamListener<String, ObjectRecord<String, Object>> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    @SneakyThrows
    public void onMessage(ObjectRecord<String, Object> record) {
        //System.out.println(InetAddress.getLocalHost().getHostName() + " - consumed :" + record.getValue());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(record.getValue());
        String json1 = new String((byte[]) record.getValue(), StandardCharsets.UTF_8);
        System.out.println(json1);
//        String jsonString = new String((byte[])record.getValue());
//        JsonNode jsonNode = objectMapper.readTree(jsonString);
//        System.out.println(jsonNode.get("name"));
//        record.getValue();
        this.redisTemplate
                .opsForZSet()
                .incrementScore("revenue", record.getValue().toString(), 123)
                .subscribe();
        atomicInteger.incrementAndGet();
    }

    @Scheduled(fixedRate = 10000)
    public void showPublishedEventsSoFar(){
        System.out.println(
                "Total Consumed :: " + atomicInteger.get()
        );
    }

}
