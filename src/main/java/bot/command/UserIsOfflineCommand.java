package bot.command;

public class UserIsOfflineCommand implements Command {

    public UserIsOfflineCommand() {

    }

    @Override
    public String execute() {
        return "You have not login.\nPlease login in first from the website and use the link given!";
    }
}
