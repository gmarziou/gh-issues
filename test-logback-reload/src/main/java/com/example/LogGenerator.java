package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class LogGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int count = 0;

    @Scheduled(fixedRate = 5000)
    public void log() {
        logger.info("Info #{}", count);
        logger.debug("Debug #{}", count);
        count++;
    }
}
