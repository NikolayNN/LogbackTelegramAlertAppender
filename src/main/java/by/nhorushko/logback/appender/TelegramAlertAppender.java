package by.nhorushko.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class TelegramAlertAppender extends AppenderBase<ILoggingEvent> {

    public static final Marker ALERT_MARKER = MarkerFactory.getMarker("ALERT");
    public static final Marker ALERT_ERROR_MARKER = MarkerFactory.getMarker("ALERT_ERROR");

    private AlertMessageSendable alertService;

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
            alertService.sendMessage(event.getLevel().levelStr, event.getFormattedMessage());
            return;
        }
        if (marker.equals(ALERT_ERROR_MARKER)) {
            alertService.sendMessage("ERROR", event.getFormattedMessage());
        }
    }

    @Override
    public void start() {
        String errorMessage = "";
        if (isBlank(botUsername)) {
            errorMessage += "\n ! botUsername is null";
        }
        if (isBlank(botToken)) {
            errorMessage += "\n ! botToken is null";
        }
        if (isBlank(channelId)) {
            errorMessage += "\n ! channel id null";
        }
        if (isBlank(serviceName)) {
            serviceName = botUsername;
        }
        if (errorMessage.length() > 0) {
            System.err.println(errorMessage + "\n start: " + ConsoleAlert.class.getSimpleName());
            alertService = new ConsoleAlert();
            return;
        }
        alertService = new TelegramBotAlert(botUsername, botToken, channelId, serviceName);
    }

    private boolean isBlank(String s){
        return s == null || s.trim().length() == 0;
    }
}
