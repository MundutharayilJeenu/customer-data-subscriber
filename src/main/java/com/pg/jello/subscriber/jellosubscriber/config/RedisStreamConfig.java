package com.pg.jello.subscriber.jellosubscriber.config;

import com.pg.jello.subscriber.jellosubscriber.bean.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import  org.springframework.data.redis.stream.Subscription;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    @Value("${stream.key:cdpStream}")
    private String streamKey;

    @Autowired
    private StreamListener<String, ObjectRecord<String, Object>> streamListener;

    @Bean
    public Subscription subscription(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        var options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(Object.class)
                .build();
        var listenerContainer = StreamMessageListenerContainer
                .create(redisConnectionFactory, options);
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(streamKey, InetAddress.getLocalHost().getHostName()),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                streamListener);
        listenerContainer.start();
        return subscription;
    }

}
