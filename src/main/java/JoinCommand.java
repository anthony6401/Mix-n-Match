public class JoinCommand implements Command {
    private final String username;
    private final Integer chatTime;
    private final ClientOrder co;

    JoinCommand(String username, Integer chatTime, ClientOrder co) {
        this.username = username;
        this.chatTime = chatTime;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "The group that you trying to join is not available anymore.";
        }

        if (!co.isWithinTimeLimit(chatTime)) {
            return "The time limit has passed!";
        }

        co.addUser(username);
        return "Successfully join a group!\n" +
                "Here is the group invite link: " + co.getInviteLink();

    }
}
