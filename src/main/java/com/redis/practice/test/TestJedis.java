package com.redis.practice.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Iterator;

/**
 * @author
 **/
public class TestJedis {

    public static void main(String[] args) {

        //建立连接
        Jedis client = new Jedis("47.104.147.118", 6379);

        //测试联通信
        System.out.printf(client.ping());

        client.set("k1", "v1");
        client.set("k2", "v3");
        client.set("k3", "v2");

        Iterator<String> keys = client.keys("*").iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println("key-->" + key);
        }

        //操作事务开启
        Transaction transaction = client.multi();
        transaction.set("transaction1", "事务数据1");
        transaction.set("transaction2", "事务数据2");

        transaction.exec(); //事务执行
        transaction.discard();//事务回滚

        //锁定某个key  提交时 若已经发生改变则失败
        client.watch("k1");

    }


}
