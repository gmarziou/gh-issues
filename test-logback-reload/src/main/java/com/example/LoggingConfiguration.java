package com.example;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import net.logstash.logback.appender.LogstashSocketAppender;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Configuration
public class LoggingConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);

    private JHipsterProperties properties;
    private LogstashConfigurator logstashConfigurator;

    @Inject
    public void setLogstashConfigurator(LogstashConfigurator logstashConfigurator) {
        this.logstashConfigurator = logstashConfigurator;
    }

    @Inject
    public void setProperties(JHipsterProperties properties) {
        this.properties = properties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (properties.getLogging().getLogstash().isEnabled()) {
            logstashConfigurator.addLogstashAppender();
            log.info("Added Logstash Appender");
        }
    }
}
