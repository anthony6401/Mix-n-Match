public class LogoutCommand implements Command {
    private final String username;

    LogoutCommand(String username) {
        this.username = username;
    }

    @Override
    public String execute() {
        db.updateOffline(username);
        return "Successfully logout!";
    }
}
