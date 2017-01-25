package com.mercury.platform.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Frost on 25.01.2017.
 */
public class MercuryServerConfig {


    private static final Logger LOGGER = LogManager.getLogger(MercuryServerConfig.class);

    private static MercuryServerConfig ourInstance = new MercuryServerConfig();

    public static MercuryServerConfig getInstance() {
        return ourInstance;
    }


    private Properties properties;
    private int disruptorBufferSize;
    private int port;
    private int threadsCount;

    private MercuryServerConfig() {
        this.properties = new Properties();
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            properties.load(new FileReader("config.properties"));
            this.initConfig();
        } catch (IOException e) {
            LOGGER.error("Unable to load server configuration. Using default config");
            this.initConfigByDefault();
        }
    }

    private void initConfigByDefault() {
        this.disruptorBufferSize = 1024;
        this.port = 10_000;
        this.threadsCount = 50;
    }

    private void initConfig() {
        this.disruptorBufferSize = Integer.valueOf(properties.getProperty("mercury.disruptor.buffer.size" , "1024"));
        this.port = Integer.valueOf(properties.getProperty("mercury.server.port" , "10000"));
        this.threadsCount = Integer.valueOf(properties.getProperty("mercury.server.threads.count" , "50"));
    }


    public int disruptorBufferSize() {
        return disruptorBufferSize;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public int getPort() {
        return port;
    }
}
