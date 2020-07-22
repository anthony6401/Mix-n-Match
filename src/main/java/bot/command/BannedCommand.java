package bot.command;

public class BannedCommand implements Command {

    public final int telegram_id;
    public final Integer time;

    public BannedCommand(int telegram_id, Integer time) {
        this.telegram_id = telegram_id;
        this.time = time;
    }

    @Override
    public String execute() {
        int bannedTime = db.getBannedTime(telegram_id);
        int bannedTimeLeft = bannedTime - time;
        return "You are banned! " + Math.abs(bannedTimeLeft) + " seconds left until unban.";
    }
}
