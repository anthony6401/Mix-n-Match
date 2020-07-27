package bot.command;

public class StartCommand implements Command {
    private final String telegramCode;
    private final Integer telegram_id;

    public StartCommand(String telegramCode, Integer telegram_id) {
        this.telegramCode = telegramCode;
        this.telegram_id = telegram_id;
    }

    @Override
    public String execute() {
        if (telegramCode == null) {
            return "You didn't put your telegram identifier. Please retrieve it from the website!";
        } else {
            if (db.containsUser(telegramCode)) {
                db.addTelegramInformation(telegramCode, telegram_id);
                db.updateOffline(telegram_id);
                db.updateOnline(telegramCode);
                return "You have successfully login!\n" +
                        "To start using this bot, please create a group and invite the bot into it!\n" +
                        "To learn more how to use the bot and all the commands, " +
                        "please use /help and /commandlist";
            } else {
                return "Please login using the website! Here is the website link https://mix-n-match-orbital.herokuapp.com";
            }
        }
    }

}
