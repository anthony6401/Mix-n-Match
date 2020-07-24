package bot.command;

public class HistoryCommand implements Command {
    private final String username;
    private final Integer telegram_id;

    public HistoryCommand(String username, Integer telegram_id) {

        this.username = username;
        this.telegram_id = telegram_id;
    }


    @Override
    public String execute() {
        int userID = db.getUserID(telegram_id);

        if (userID == -1) {
            return "You do not have any order history!";
        }

        return username + "'s history:\n" + db.getHistory(telegram_id) + "\n" +
                "For more information, please visit the website!";
    }

}
