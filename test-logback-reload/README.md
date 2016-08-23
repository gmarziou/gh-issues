This project is a prototype for a possible solution to #github.com/spring-projects/spring-boot/issues/6688 and #jhipster/jhipster-console/issues/41

- devtools are not used to be sure that modifying src/main/resources/logback-spring.xml will not reload the app context
- run the app with mvn spring-boot:run
- while running, change logger level in src/main/resources/config/logback-spring.xml

Configure the logstash host and port in application.yml
