import bot.command.*;
import bot.utility.BotIdentity;
import bot.utility.ClientOrder;
import bot.utility.DatabaseCon;
import bot.utility.StringSplitter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MixnMatchBot extends TelegramLongPollingBot {
    public static int MAX_TELEGRAM_MESSAGE_LENGTH = 4096;
    public Map<Long, ClientOrder> map = new HashMap<>();
    DatabaseCon db = new DatabaseCon();

    @Override
    public void onUpdateReceived(Update update) {
        Long chat_id = update.getMessage().getChatId();
        Integer telegram_id = update.getMessage().getFrom().getId();
        SendMessage message = new SendMessage();

        // Setting the chat id to where the message wil be sent.
        message.setChatId(chat_id);

        // Seperate the command and the argument
        String[] query = update.getMessage().getText().split("\\s", 2);
        // bot.Command.Command inputted
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
            if (!db.isOnline(telegram_id)) {
                command = new UserIsOfflineCommand();
            } else {
                // orderfrom command
                if (commandString.equals("/orderfrom")) {
                    command = new OrderFromCommand(map, arg, chat_id);
                // orderto command
                } else if (commandString.equals("/orderto")) {
                    command = new OrderToCommand(map, arg, chat_id);
                } else if (commandString.equals("/orderstatus")) {
                    ClientOrder co = map.get(chat_id);
                    command = new CheckOrderStatusCommand(co);
                } else if (commandString.equals("/invitelink")) {
                    command = new InviteLinkCommand(map, arg, chat_id);
                } else if (commandString.equals("/settime")) {
                    command = new SetTimeCommand(map, arg, chat_id);
                } else if (commandString.equals("/finalizeorder")) {
                    Integer time = update.getMessage().getDate();
                    String username = update.getMessage().getFrom().getUserName();
                    ClientOrder co = map.get(chat_id);
                    command = new FinalizeOrderCommand(username, telegram_id, chat_id, time, co);
                } else if (commandString.equals("/reset")) {
                    command = new ResetCommand(map, chat_id);
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
                    command = new AddCommand(arg, telegram_id, co);
                } else if (commandString.equals("/remove")) {
                    ClientOrder co = map.get(chat_id);
                    command = new RemoveCommand(arg, telegram_id, co);
                } else if (commandString.equals("/removeall")) {
                    ClientOrder co = map.get(chat_id);
                    command = new RemoveAllCommand(telegram_id, co);
                } else if (commandString.equals("/deliverycost")) {
                    ClientOrder co = map.get(chat_id);
                    command = new DeliveryCostCommand(arg, co);
                } else if (commandString.equals("/commandlist")) {
                    command = new CommandListCommand();
                } else if (commandString.equals("/orderee")) { // not yet
                    ClientOrder co = map.get(chat_id);
                    command = new OrdereeCommand("abc", co);
                } else if (commandString.equals("/verifyuser")) { // not yet
                    command = new VerifyUserCommand();
                } else if (commandString.equals("/unverifyuser")) { // not yet
                    command = new UnverifyUserCommand();
                } else if (commandString.equals("/exitgroup")) { // not yet
                    command = new ExitGroupCommand();
                } else if (commandString.equals("/seemap")) {
                    System.out.println(map.toString());
                } else if (commandString.equals("/history")) {
                    String username = update.getMessage().getFrom().getUserName();
                    command = new HistoryCommand(username, telegram_id);
                } else {
                    command = new NotACommand();
                }
            }
        // If the user tries message the bot through private message
        } else if (chat_id > 0) {
            // Start of the bot using the identifier gotten from the website.
            if (commandString.equals("/start")) {
                // If the user tries to use the bot without logging in from the website
                command = new StartCommand(arg, telegram_id);
                // Join command
            } else if (commandString.equals("/join")) {
                Integer time = update.getMessage().getDate();
                String username = update.getMessage().getFrom().getUserName();
                ClientOrder co = map.get(Long.valueOf(arg));
                command = new JoinCommand(username, telegram_id, time, co);
            // Logout command
            } else if (commandString.equals("/logout")) {
                command = new LogoutCommand(telegram_id);
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

                    //bot can't send multiple message under one second
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
