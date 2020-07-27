package bot.command;

public class UserIsOfflineCommand implements Command {

    public UserIsOfflineCommand() {

    }

    @Override
    public String execute() {
        return "You have not login.\nPlease login in first from the https://mix-n-match-orbital.herokuapp.com and use the link given!";
    }
}
