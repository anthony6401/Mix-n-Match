package bot.command;

public class NoUsernameCommand implements Command {

    @Override
    public String execute() {
        return "You don't have any telegram username! Please go to telegram settings and set your username.";
    }
}
