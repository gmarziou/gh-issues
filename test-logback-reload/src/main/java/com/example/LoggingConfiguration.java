package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.inject.Inject;

@Configuration
public class LoggingConfiguration {

    private final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);

    private JHipsterProperties properties;

    private LogstashConfigurator logstashConfigurator;

    private LogbackLoggerContextListener contextListener;

    @Inject
    public void setProperties(JHipsterProperties properties) {
        this.properties = properties;
    }

    @Inject
    public void setLogstashConfigurator(LogstashConfigurator logstashConfigurator) {
        this.logstashConfigurator = logstashConfigurator;
    }

    @Inject
    public void setContextListener(LogbackLoggerContextListener contextListener) {
        this.contextListener = contextListener;
    }

}
