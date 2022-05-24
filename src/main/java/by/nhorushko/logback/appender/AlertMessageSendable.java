package by.nhorushko.logback.appender;

public interface AlertMessageSendable {
    void sendMessage(String type, String message);
}
