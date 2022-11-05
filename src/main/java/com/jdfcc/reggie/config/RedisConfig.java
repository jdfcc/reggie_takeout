package com.jdfcc.reggie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        //默认的key序列化器为: JDKSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer()); //重新设置序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); //重新设置序列化器

        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }
}
