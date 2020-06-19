public class HistoryCommand implements Command {
    private final String username;

    HistoryCommand(String username) {
        this.username = username;
    }


    @Override
    public String execute() {
        int userID = db.getUserID(username);

        if (userID == -1) {
            return "You do not have any order history!";
        }

        return username + "'s history:\n" + db.getHistory(username);
    }

}
