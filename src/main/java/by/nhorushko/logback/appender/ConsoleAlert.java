package by.nhorushko.logback.appender;

public class ConsoleAlert implements AlertMessageSendable {

    @Override
    public void sendMessage(String type, String message) {
        System.out.printf("ALERT: %s : %s%n", type, message);
    }
}
