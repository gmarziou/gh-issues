package com.example;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LogbackLoggerContextListener extends ContextAwareBase implements LoggerContextListener {

    private JHipsterProperties properties;

    private LogstashConfigurator logstashConfigurator;

    @Inject
    public void setProperties(JHipsterProperties properties) {
        this.properties = properties;
    }

    @Inject
    public void setLogstashConfigurator(LogstashConfigurator logstashConfigurator) {
        this.logstashConfigurator = logstashConfigurator;
    }


    public void register() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        setContext(context);
        context.addListener(this);

        addLogstashAppender(context);
    }

    @Override
    public boolean isResetResistant() {
        return true;
    }

    @Override
    public void onStart(LoggerContext context) {
        addLogstashAppender(context);
    }

    @Override
    public void onReset(LoggerContext context) {
        addLogstashAppender(context);
    }

    @Override
    public void onStop(LoggerContext context) {
    }

    @Override
    public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
    }

    private void addLogstashAppender(LoggerContext context) {
        if (properties.getLogging().getLogstash().isEnabled()) {
            logstashConfigurator.addLogstashAppender(context);
            addInfo("Added Logstash Appender");
        }
    }

}
