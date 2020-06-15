public class UserIsOfflineCommand implements Command{

    UserIsOfflineCommand() {

    }

    @Override
    public String execute() {
        return "You have not login.\nPlease login in first from the website and use the link given!";
    }
}
