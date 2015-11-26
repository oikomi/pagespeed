package com.baidu.uaq.pagespeed.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by baidu on 15/11/23.
 */
public class Config {
    private static ClassLoader classLoader = Config.class.getClassLoader();
    private static final String configFilePath = "config/pagespeed_config.json";
    private String listenPort;
    private String redisAddr;
    private int redisPort;

    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucket;

    private String taskList;

    private static Config _instance;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }


    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getListenPort() {
        return listenPort;
    }

    public void setListenPort(String listenPort) {
        this.listenPort = listenPort;
    }

    public String getRedisAddr() {
        return redisAddr;
    }

    public void setRedisAddr(String redisAddr) {
        this.redisAddr = redisAddr;
    }


    public String getTaskList() {
        return taskList;
    }

    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }

    static {
        Gson gson = new Gson();
        FileInputStream configIn = null;
        try {
            configIn = new FileInputStream(classLoader.getResource(configFilePath).getFile());
            _instance = gson.fromJson(IOUtils.toString(configIn), Config.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(configIn);
        }
    }

    public static Config getInstance() {
        return _instance;
    }

}
