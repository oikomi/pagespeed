package com.baidu.uaq.pagespeed.db;

import com.baidu.uaq.pagespeed.config.Config;
import redis.clients.jedis.Jedis;

/**
 * Created by miaohong01 on 15/11/13.
 */
public class Redis {
    private Config config = Config.getInstance();
    private Jedis jedis;
    private String redisAddr;
    private int redisPort;

    public Redis(String redisAddr, int redisPort) {
        this.redisAddr = redisAddr;
        this.redisPort = redisPort;
        conn();
    }

    private void conn() {
        this.jedis = new Jedis(redisAddr, redisPort);
    }

    public void addKV(String key, String value) {
        jedis.set(key, value);
        jedis.expire(key, 3600 * 20);
    }

}
