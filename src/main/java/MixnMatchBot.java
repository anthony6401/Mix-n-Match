import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import java.util.*;

public class MixnMatchBot extends TelegramLongPollingBot {
    public static Map<Long, ClientOrder> map = new HashMap<>();
    DatabaseCon db = new DatabaseCon();

    @Override
    public void onUpdateReceived(Update update) {
        Long chat_id = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        SendMessage message = new SendMessage();

        // Setting the chat id to where the message wil be sent.
        message.setChatId(chat_id);

        // Seperate the command and the argument
        String[] query = update.getMessage().getText().split("\\s", 2);
        // Command inputted
        String commandString = query[0];
        String arg = null;
        if (query.length != 1) {
            arg = query[1];
        }

        Command command = null;

        // Start of the bot using the identifier gotten from the website.
        if (commandString.equals("/start")) {
            // If the user tries to use the bot without logging in from the website
            command = new StartCommand(arg, username);
        // Using the command from the telegram group.
        } else if (chat_id < 0) {
            // Checking if the user is login or not.
            if (!db.isOnline(username)) {
                command = new UserIsOfflineCommand();
            } else {
                // orderfrom command
                if (commandString.equals("/orderfrom")) {
                    command = new OrderFromCommand(arg, chat_id);
                // orderto command
                } else if (commandString.equals("/orderto")) {
                    command = new OrderToCommand(arg, chat_id);
                } else if (commandString.equals("/orderstatus")) {
                    ClientOrder co = map.get(chat_id);
                    command = new CheckOrderStatusCommand(co);
                } else if (commandString.equals("/reset")) {
                    ClientOrder co = map.get(chat_id);
                    command = new ResetCommand(co);
                } else if (commandString.equals("/categorylist")) {
                    command = new CategoryListCommand(arg);
                } else if (commandString.equals("/restaurantlist")) {
                    command = new RestaurantListCommand(arg);
                } else if (commandString.equals("/menu")) {
                    command = new MenuCommand(arg);
                } else if (commandString.equals("/searchrestaurant")) {
                    command = new SearchRestaurantCommand(arg);
                } else if (commandString.equals("/add")) {
                    ClientOrder co = map.get(chat_id);
                    command = new AddCommand(arg, username, co);
                } else if (commandString.equals("/logout")) {
                    command = new LogoutCommand(username);
                } else if (commandString.equals("/seemap")) {
                    System.out.println(map);
                } else {
                    command = new NotACommand();
                }
            }
        // If the user tries message the bot through private message
        } else if (chat_id > 0) {
            // Join command
            if (commandString.equals("/join")) {
                ClientOrder co = map.get(Long.valueOf(arg));
                command = new JoinCommand(username, co);

            // If the user tries to use commands available only on groups
            } else {
                command = new NotInGroupCommand();
            }
        }


        // Executing the command. The command may be null if the user's message is not starting with "/"
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
        return BotIdentity.username;
    }

    @Override
    public String getBotToken() {
        return BotIdentity.botToken;
    }
}
