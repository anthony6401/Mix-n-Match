public class JoinCommand implements Command {
    private final String username;
    private final ClientOrder co;

    JoinCommand(String username, ClientOrder co) {
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "The group that you trying to join is not available anymore.";
        } else {
            co.addUser(username);
            return "Successfully join a group!";
        }
    }
}
