public class RemoveAllCommand implements Command {
    private final String username;
    private final ClientOrder co;

    RemoveAllCommand(String username, ClientOrder co) {
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        try {
            co.deleteAllOrderFromUser(username);
            return "Successfully removing all the orders!";
        } catch (NoSuchUserExistException e) {
            return "The user can't be found. Please reset using /logout " +
                    "and login using the link provided in the website!";
        }
    }
}
