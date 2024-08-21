package Controller;


import Model.Person;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.util.HashMap;

public class RedisController {
    private static Jedis jedis;

    public void redisConnection(){
        long startTime = System.currentTimeMillis();
        jedis = new Jedis("localhost", 6379);
        System.out.println("Connection Made");// Connect to Red
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/ 1000;
        System.out.println("Time to read: " + duration+ " seconds");
    }
    public void addPeopleInRedis(Person person){
        jedis.hset("people", String.valueOf(person.getID()),person.toString());

    }
    public void deletePeopleInRedis(Person person){
        jedis.hdel("people", String.valueOf(person.getID()));
    }
    public void readPeopleInRedis(Person person){
        jedis.hget("people", String.valueOf(person.getID()));
        System.out.println(jedis.hget("people", String.valueOf(person.getID())));
    }
    public void updatePeopleInRedis(Person person){
        jedis.hdel("people", String.valueOf(person.getID()));
        jedis.hset("people", String.valueOf(person.getID()),person.toString());
        System.out.println(jedis.hget("people", String.valueOf(person.getID())));
    }

}
