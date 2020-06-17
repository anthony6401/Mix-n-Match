public class StartCommand implements Command {
    private final String telegramCode;
    private final String username;
    private final long chat_id;

    StartCommand(String telegramCode, String username, long chat_id) {
        this.telegramCode = telegramCode;
        this.username = username;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (telegramCode == null) {
            return "You didn't put your telegram identifier. Please retrieve it from the website!";
        } else {
            if (db.containsUser(telegramCode)) {
                db.addTelegramInformation(telegramCode, username, chat_id);
                db.updateOffline(username);
                db.updateOnline(telegramCode);
                return "You have successfully login!\n" +
                        "To start using this bot, please create a group and invite the bot into it!\n" +
                        "To learn more how to use the bot and all the commands, " +
                        "please use /help and /commandlist";
            } else {
                return "Please login using the website!";
            }
        }
    }

}
