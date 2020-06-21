package bot.command;

public class LogoutCommand implements Command {
    private final Integer telegram_id;

    public LogoutCommand(Integer telegram_id) {
        this.telegram_id = telegram_id;
    }

    @Override
    public String execute() {
        db.updateOffline(telegram_id);
        return "Successfully logout!";
    }
}
