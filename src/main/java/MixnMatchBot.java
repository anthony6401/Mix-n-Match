import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import java.util.*;

public class MixnMatchBot extends TelegramLongPollingBot {
    public static int MAX_TELEGRAM_MESSAGE_LENGTH = 4096;
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

        if (commandString.equals("/help")) {
            command = new HelpCommand();
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
                } else if (commandString.equals("/invitelink")) {
                    command = new InviteLinkCommand(arg, chat_id);
                } else if (commandString.equals("/settime")) {
                    command = new SetTimeCommand(arg, chat_id);
                } else if (commandString.equals("/finalizeorder")) {
                    Integer time = update.getMessage().getDate();
                    ClientOrder co = map.get(chat_id);
                    command = new FinalizeOrderCommand(username, chat_id, time, co);
                } else if (commandString.equals("/reset")) {
                    command = new ResetCommand(chat_id);
                } else if (commandString.equals("/categorylist")) {
                    command = new CategoryListCommand();
                } else if (commandString.equals("/restaurantlist")) {
                    command = new RestaurantListCommand(arg);
                } else if (commandString.equals("/menu")) {
                    command = new MenuCommand(arg);
                } else if (commandString.equals("/searchrestaurant")) {
                    command = new SearchRestaurantCommand(arg);
                } else if (commandString.equals("/add")) {
                    ClientOrder co = map.get(chat_id);
                    command = new AddCommand(arg, username, co);
                } else if (commandString.equals("/remove")) {
                    ClientOrder co = map.get(chat_id);
                    command = new RemoveCommand(arg, username, co);
                } else if (commandString.equals("/removeall")) {
                    ClientOrder co = map.get(chat_id);
                    command = new RemoveAllCommand(username, co);
                } else if (commandString.equals("/deliverycost")) {
                    ClientOrder co = map.get(chat_id);
                    command = new DeliveryCostCommand(arg, co);
                } else if (commandString.equals("/commandlist")) {
                    command = new CommandListCommand();
                } else {
                    command = new NotACommand();
                }
            }
        // If the user tries message the bot through private message
        } else if (chat_id > 0) {

            // Start of the bot using the identifier gotten from the website.
            if (commandString.equals("/start")) {
                // If the user tries to use the bot without logging in from the website
                command = new StartCommand(arg, username, chat_id);
                // Join command
            } else if (commandString.equals("/join")) {
                Integer time = update.getMessage().getDate();
                ClientOrder co = map.get(Long.valueOf(arg));
                command = new JoinCommand(username, time, co);
            // Logout command
            } else if (commandString.equals("/logout")) {
                command = new LogoutCommand(username);
            // If the user tries to use commands available only on groups
            } else {
                command = new NotInGroupCommand();
            }
        }


        // Executing the command. The command may be null if the user's message is not starting with "/"
        if (command != null) {
            String botMessage = command.execute();

            message.setChatId(chat_id);
            // If the botMessage is too long to be sent from the telegram bot
            if (botMessage.length() > MAX_TELEGRAM_MESSAGE_LENGTH) {
                List<String> splittedMessage = StringSplitter.split(botMessage);
                for (String msg : splittedMessage) {
                    message.setText(msg);
                    sendMessage(message);
                }
            // BotMessage is short enough to be sent from the telegram bot
            } else {
                message.setText(botMessage);
                sendMessage(message);
            }

            if (command instanceof FinalizeOrderCommand) {
                FinalizeOrderCommand foCommand = (FinalizeOrderCommand) command;
                List<SendMessage> messageList = foCommand.notifyOnlineUser();

                for (SendMessage msg : messageList) {
                    System.out.println(msg.getChatId());
                    sendMessage(msg);
                }
            }
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
