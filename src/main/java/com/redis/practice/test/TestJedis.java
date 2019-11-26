package com.redis.practice.test;

import redis.clients.jedis.Jedis;

/**
 * @author
 **/
public class TestJedis {

    public static void main(String[] args) {

        //建立连接
        Jedis client = new Jedis("127.0.0.1", 6379);


    }


}
