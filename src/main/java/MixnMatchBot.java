import bot.command.*;
import bot.utility.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
        Integer time = update.getMessage().getDate();
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

        System.out.println(arg);

        Command command = null;

        if (db.isBanned(telegram_id, time)) {
            command = new BannedCommand(telegram_id, time);
        } else {
            if (commandString.startsWith("/help")) {
                command = new HelpCommand();
            } else if (commandString.startsWith("/categorylist")) {
                command = new CategoryListCommand();
            } else if (commandString.startsWith("/restaurantlist")) {
                command = new RestaurantListCommand(arg);
            } else if (commandString.startsWith("/menu")) {
                command = new MenuCommand(arg);
            } else if (commandString.startsWith("/searchrestaurant")) {
                command = new SearchRestaurantCommand(arg);
            } else if (commandString.startsWith("/commandlist")) {
                command = new CommandListCommand();
            } else if (commandString.startsWith("/foodinfo")) {
                command = new FoodInfoCommand(arg, chat_id);
            } else if (commandString.startsWith("/history")) {
                String username = update.getMessage().getFrom().getUserName();
                command = new HistoryCommand(username, telegram_id);
            } else if (chat_id < 0) {

                System.out.println(chat_id);
                // Checking if the user is login or not.
                if (!db.isOnline(telegram_id)) {
                    command = new UserIsOfflineCommand();
                } else {
                    if (commandString.startsWith("/orderstatus")) {
                        ClientOrder co = map.get(chat_id);
                        command = new CheckOrderStatusCommand(co, chat_id);
                    } else if (db.containsOrderId(chat_id)) {
                        command = new GroupIsUsedCommand();
                     // orderfrom command
                    } else if (commandString.startsWith("/orderfrom")) {
                        command = new OrderFromCommand(map, arg, chat_id);
                     // orderto command
                    } else if (commandString.startsWith("/orderto")) {
                        command = new OrderToCommand(map, arg, chat_id);
                    } else if (commandString.startsWith("/invitelink")) {
                        command = new InviteLinkCommand(map, arg, chat_id);
                    } else if (commandString.startsWith("/ordertime")) {
                        command = new OrderTimeCommand(map, arg, chat_id);
                    } else if (commandString.startsWith("/paymenttime")) {
                        command = new PaymentTimeCommand(map, arg, chat_id);
                    } else if (commandString.startsWith("/finalizeorder")) {
                        String username = update.getMessage().getFrom().getUserName();
                        ClientOrder co = map.get(chat_id);
                        command = new FinalizeOrderCommand(username, telegram_id, chat_id, time, co);
                    } else if (commandString.startsWith("/reset")) {
                        command = new ResetCommand(map, chat_id);
                    } else if (commandString.startsWith("/add")) {
                        ClientOrder co = map.get(chat_id);
                        command = new AddCommand(arg, telegram_id, co);
                    } else if (commandString.startsWith("/removeall")) {
                        ClientOrder co = map.get(chat_id);
                        command = new RemoveAllCommand(telegram_id, co);
                    } else if (commandString.startsWith("/remove")) {
                        ClientOrder co = map.get(chat_id);
                        command = new RemoveCommand(arg, telegram_id, co);
                    } else if (commandString.startsWith("/deliverycost")) {
                        ClientOrder co = map.get(chat_id);
                        command = new DeliveryCostCommand(arg, co);
                    } else if (commandString.startsWith("/verifyuser")) {
                        ClientOrder co = map.get(chat_id);
                        command = new VerifyUserCommand(arg, telegram_id, co);
                    } else if (commandString.startsWith("/unverifyuser")) {
                        ClientOrder co = map.get(chat_id);
                        command = new UnverifyUserCommand(arg, telegram_id, co);
                    } else if (commandString.startsWith("/exitgroup")) {
                        ClientOrder co = map.get(chat_id);
                        command = new ExitGroupCommand(telegram_id, co);
                    } else {
                        command = new NotACommand();
                    }
                }
                // If the user tries message the bot through private message
            } else if (chat_id > 0) {
                // Start of the bot using the identifier gotten from the website.
                if (commandString.startsWith("/start")) {
                    // If the user tries to use the bot without logging in from the website
                    command = new StartCommand(arg, telegram_id);
                    // Join command
                } else if (commandString.startsWith("/join")) {
                    String username = update.getMessage().getFrom().getUserName();
                    ClientOrder co = map.get(Long.valueOf(arg));
                    command = new JoinCommand(username, telegram_id, time, co);
                    // Logout command
                } else if (commandString.startsWith("/logout")) {
                    command = new LogoutCommand(telegram_id);
                    // If the user tries to use commands available only on groups
                } else {
                    command = new NotInGroupCommand();
                }
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
                if (command instanceof FoodInfoCommand) {
                    FoodInfoCommand fiCommand = (FoodInfoCommand) command;
                    if (fiCommand.isSendPic()) {
                        SendPhoto photoMessage = fiCommand.getPhotoMessage();
                        photoMessage.setCaption(botMessage);
                        sendPhoto(photoMessage);
                    } else {
                        message.setText(botMessage);
                        sendMessage(message);
                    }
                } else {
                    message.setText(botMessage);
                    sendMessage(message);
                }
            }

            // Notification purposes
            if (command instanceof FinalizeOrderCommand) {
                ClientOrder co = map.get(chat_id);

                // If the finalize order command is the first one
                if (co.getStartTime().equals(update.getMessage().getDate())) {
                    FinalizeOrderCommand foCommand = (FinalizeOrderCommand) command;
                    List<SendMessage> messageList = foCommand.notifyOnlineUser();

                    // Sending the message to everyone near to where the order is
                    for (SendMessage msg : messageList) {
                        sendMessage(msg);
                    }

                    // To send a message that for the order is up and proceed to payment

                    // Sending the message for the payment order is up
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            co.setExceedOrderTimeLimitToTrue();
                            OrderTimeLimitExceedCommand otlCommand = new OrderTimeLimitExceedCommand(co);
                            String botMessage = otlCommand.execute();
                            message.setText(botMessage);
                            sendMessage(message);

                            // Sending the message for the payment period is up
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    co.setExceedPaymentTimeLimitToTrue();
                                    Command paymentTimeLimitCommand =
                                            new PaymentTimeLimitExceedCommand(chat_id, co);
                                    String botMessage = paymentTimeLimitCommand.execute();
                                    message.setText(botMessage);
                                    sendMessage(message);
                                }
                            },  co.getPaymentTimeLimit() * DateTime.SECONDS_TO_MILLISECONDS);


                        }
                    }, co.getOrderTimeLimit() * DateTime.SECONDS_TO_MILLISECONDS);
                }
            }

            if (command instanceof DeliveryCostCommand) {
                DeliveryCostCommand dcCommand = (DeliveryCostCommand) command;

                List<SendMessage> messageList = dcCommand.sendPaymentInfo();

                for (SendMessage msg : messageList) {
                    sendMessage(msg);
                }
            }
        }
    }

    private void sendPhoto(SendPhoto photoMessage) {
        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
