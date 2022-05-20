Telegram appender

application.properties
```
logback.logger.com.locator.level=ERROR
logback.logger.appender.telegram.bot-username=username_bot
logback.logger.appender.telegram.bot-token=123:123231312
logback.logger.appender.telegram.channel-id=-123123213
logback.logger.appender.telegram.service-name=service-name:TAG
```

logback.xml
```
<configuration>

    <springProperty scope="context" name="profile" source="spring.profiles.active"/>
    <property resource="application-${profile}.properties" />
    <property resource="application.properties" />

    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <target>System.out</target>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="console"/>
    </root>

    <appender name="telegram" class="com.locator.TelegramAlertAppender">
        <botUsername>${logback.logger.appender.telegram.bot-username}</botUsername>
        <botToken>${logback.logger.appender.telegram.bot-token}</botToken>
        <channelId>${logback.logger.appender.telegram.channel-id}</channelId>
        <serviceName>${logback.logger.appender.telegram.service-name}</serviceName>
    </appender>

    <logger name="com.locator" level="INFO">
        <appender-ref ref="telegram"/>
    </logger>

</configuration>
```