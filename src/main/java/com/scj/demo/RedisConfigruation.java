package com.scj.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by shengchaojie on 2018/2/4.
 */
@Configuration
public class RedisConfigruation {

    @Bean
    public RedisTemplate<String,Long> stringLongRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Long> template = new RedisTemplate<String, Long>();
        template.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Long> longRedisSerializer = new LongRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(longRedisSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(longRedisSerializer);
        return template;
    }

}
