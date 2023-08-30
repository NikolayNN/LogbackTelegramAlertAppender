package by.nhorushko.logback.appender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TelegramRatedSender {
    private static final Logger log = LoggerFactory.getLogger(TelegramRatedSender.class);
    /**
     * A map to track the last time a log was sent for each key.
     */
    Map<String, Long> lastLogSend = new ConcurrentHashMap<>();

    /**
     * The interval (in seconds) to wait before sending another log for the same key.
     */
    long sendIntervalSeconds;

    /**
     * Sends an error message to the log if the specified interval has passed since the last error for the key.
     *
     * @param key     The key representing the type or source of the error.
     * @param message The error message template to log.
     * @param params  Parameters for the error message template.
     * @return {@code true} if the error was logged, {@code false} otherwise.
     */
    public boolean sendError(String key, String message, Object... params) {
        return logMessage(LogLevel.ERROR, key, message, params);
    }

    public boolean sendWarn(String key, String message, Object... params) {
        return logMessage(LogLevel.WARN, key, message, params);
    }

    public boolean sendInfo(String key, String message, Object... params) {
        return logMessage(LogLevel.INFO, key, message, params);
    }

    private boolean logMessage(LogLevel logLevel, String key, String message, Object... params) {
        long lastSentTime = lastLogSend.getOrDefault(key, 0L);
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (currentTimeSeconds - lastSentTime > sendIntervalSeconds) {
            switch (logLevel) {
                case ERROR:
                    log.error(TelegramAlertAppender.ALERT_MARKER, message, params);
                    break;
                case WARN:
                    log.warn(TelegramAlertAppender.ALERT_MARKER, message, params);
                    break;
                case INFO:
                    log.info(TelegramAlertAppender.ALERT_MARKER, message, params);
                    break;
            }
            lastLogSend.remove(key);
            return true;
        }
        return false;
    }

    private enum LogLevel {
        ERROR, WARN, INFO
    }
}
