import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import java.util.*;

public class MixnMatchBot extends TelegramLongPollingBot {
    Map<Long, ClientOrder> map = new HashMap<>();
    DatabaseCon db = new DatabaseCon();

    @Override
    public void onUpdateReceived(Update update) {
        Long chat_id = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        SendMessage message = new SendMessage();

        message.setChatId(chat_id);

        String[] query = update.getMessage().getText().split("\\s", 2);
        String commandString = query[0];
        Command command = null;

        if (commandString.equals("/start")) {
            if (query.length == 1) {
                command = new StartCommand();
            } else {
                String arg = query[1];
                command = new StartCommand(arg, username);
            }
        }

        if (command != null) {
            String botMessage = command.execute();
            message.setChatId(chat_id);
            message.setText(botMessage);
            sendMessage(message);
        }

    }

    private void sendMessage(SendMessage message) {
        try {
            executeAsync(message, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
                    System.out.println(message);
                }

                @Override
                public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
                    System.out.println("there is an error: " + e);

                }

                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                    System.out.println("there is an exception: " + e);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "MixnMatchBot";
    }

    @Override
    public String getBotToken() {
        return "1242060435:AAHiEreNvJ5Qw3cud-TOef7IULiUwz8wpxg";
    }
}
