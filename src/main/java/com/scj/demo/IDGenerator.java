package com.scj.demo;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shengchaojie on 2018/2/4.
 */
@Component
public class IDGenerator {

    private static final String Prefix = "Test";

    private ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat get() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    @Autowired
    private RedisTemplate<String,Long> redisTemplate;


    public  String nextID(){
        String key = Prefix+simpleDateFormatThreadLocal.get().format(new Date());
        Long existedID = redisTemplate.opsForValue().get(key);
        if(existedID!=null){
            redisTemplate.opsForValue().set(key,existedID+1);
            return key+String.format("%04d",existedID+1);
        }else{
            redisTemplate.opsForValue().set(key,1L);
            return key+"0001";
        }
    }

    public String nextIDLua(){
        String key = Prefix+simpleDateFormatThreadLocal.get().format(new Date());
        DefaultRedisScript<String> redisScript =new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("lua/genID.lua"));
        redisScript.setResultType(String.class);
        //System.out.println(redisScript.getSha1());
        return redisTemplate.execute(redisScript,(RedisSerializer<?>) redisTemplate.getKeySerializer(),(RedisSerializer<String>)redisTemplate.getKeySerializer(),Lists.newArrayList(key));
    }


    public String currentID(){
        String key = Prefix+simpleDateFormatThreadLocal.get().format(new Date());
        return key+String.format("%04d",redisTemplate.opsForValue().get(key));
    }


}
