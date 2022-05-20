package by.nhorushko.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class TelegramAlertAppender extends AppenderBase<ILoggingEvent> {

    public static final Marker ALERT_MARKER = MarkerFactory.getMarker("ALERT");
    public static final Marker ALERT_ERROR_MARKER = MarkerFactory.getMarker("ALERT_ERROR");

    private TelegramBotAlert telegramBotAlert;

    private String botUsername;
    private String botToken;
    private String channelId;
    private String serviceName;

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected void append(ILoggingEvent event) {
        Marker marker = event.getMarker();
        if (marker == null) {
            return;
        }
        if (marker.equals(ALERT_MARKER)) {
            telegramBotAlert.sendMessage(event.getLevel().levelStr, event.getFormattedMessage());
            return;
        }
        if (marker.equals(ALERT_ERROR_MARKER)) {
            telegramBotAlert.sendMessage("ERROR", event.getFormattedMessage());
        }
    }

    @Override
    public void start() {
        if (botUsername == null) {
            throw new NullPointerException("botUsername is null");
        }
        if (botToken == null) {
            throw new NullPointerException("botToken is null");
        }
        if (channelId == null) {
            throw new NullPointerException("channelId is null");
        }
        if (serviceName == null) {
            throw new NullPointerException("serviceName is null");
        }
        telegramBotAlert = new TelegramBotAlert(botUsername, botToken, channelId, serviceName);
    }
}
