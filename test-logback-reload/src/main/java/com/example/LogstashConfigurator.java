package com.example;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import net.logstash.logback.appender.LogstashSocketAppender;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LogstashConfigurator {
    public static final String MY_LISTENER = "MyListener";
    private final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);

    private LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    private JHipsterProperties properties;

    @Inject
    public void setProperties(JHipsterProperties properties) {
        this.properties = properties;
    }


    public void addLogstashAppender() {
        log.info("Initializing Logstash logging");

        LogstashSocketAppender logstashAppender = new LogstashSocketAppender();
        logstashAppender.setName("LOGSTASH");
        logstashAppender.setContext(context);
        String customFields = "{\"app_name\":\"" + appName + "\",\"app_port\":\"" + serverPort + "\"," +
            "\"instance_id\":\"" + instanceId + "\"}";

        log.info("Logstash customFields: '{}', config: '{}'", customFields, properties.getLogging().getLogstash());

        // Set the Logstash appender config from JHipster properties
        logstashAppender.setSyslogHost(properties.getLogging().getLogstash().getHost());
        logstashAppender.setPort(properties.getLogging().getLogstash().getPort());
        logstashAppender.setCustomFields(customFields);

        // Limit the maximum length of the forwarded stacktrace so that it won't exceed the 8KB UDP limit of logstash
        ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
        throwableConverter.setMaxLength(7500);
        throwableConverter.setRootCauseFirst(true);
        logstashAppender.setThrowableConverter(throwableConverter);

        logstashAppender.start();

        // Wrap the appender in an Async appender for performance
        AsyncAppender asyncLogstashAppender = new AsyncAppender();
        asyncLogstashAppender.setContext(context);
        asyncLogstashAppender.setName("ASYNC_LOGSTASH");
        asyncLogstashAppender.setQueueSize(properties.getLogging().getLogstash().getQueueSize());
        asyncLogstashAppender.addAppender(logstashAppender);
        asyncLogstashAppender.start();

        context.getLogger("ROOT").addAppender(asyncLogstashAppender);

        /*
        context.addListener(new LoggerContextListener() {

            @Override
            public boolean isResetResistant() {
                return true;
            }

            private Logger getLogger(LoggerContext context) {
                return context.getLogger(MY_LISTENER);
            }

            @Override
            public void onStart(LoggerContext context) {
                getLogger(context).info("onStart");
            }

            @Override
            public void onReset(LoggerContext context) {
                getLogger(context).info("onReset");
            }

            @Override
            public void onStop(LoggerContext context) {
                getLogger(context).info("onStop");
            }

            @Override
            public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
                getLogger(context).info("onLevelChange");
            }
        });

        */
    }

}
