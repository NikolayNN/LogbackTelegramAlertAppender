package by.nhorushko.logback.appender;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Locale;

public class TelegramBotAlert extends TelegramLongPollingBot implements AlertMessageSendable {

    private final String botUsername;
    private final String botToken;
    private final String channelId;
    private final String serviceName;

    private final String info = EmojiParser.parseToUnicode(":green_heart:");
    private final String warn = EmojiParser.parseToUnicode(":yellow_heart:");
    private final String error = EmojiParser.parseToUnicode(":broken_heart:");
    private final String other = EmojiParser.parseToUnicode(":blue_heart:");

    public TelegramBotAlert(String botUsername, String botToken, String channelId, String serviceName) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.channelId = channelId;
        this.serviceName = serviceName;
        onRun();
    }

    @Override
    public void sendMessage(String type, String message) {
        if (message.length() > 4000) {
            message = message.substring(0, 4000);
        }
        String label = getLabel(type);
        String m = serviceName + "\n" + label + " - " + message;
        sendMessage(new SendMessage(channelId, m));
    }

    private String getLabel(String type) {

        switch (type.toLowerCase(Locale.ROOT)) {
            case "warn":
                return warn;
            case "error":
                return error;
            case "info":
                return info;
            default:
                return other;
        }
    }

    private void onRun() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            sendMessage("INFO", "START");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> sendMessage("WARN", "STOP")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
