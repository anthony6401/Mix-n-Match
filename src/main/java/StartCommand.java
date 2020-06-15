public class StartCommand implements Command {
    private final String arg;
    private final String username;

    StartCommand(String arg, String username) {
        this.arg = arg;
        this.username = username;
    }

    @Override
    public String execute() {
        if (arg == null) {
            return "You didn't put your telegram identifier. Please retrieve it from the website!";
        } else {
            if (db.containsUser(username)) {
                if (db.getPassword(username).equals(arg)) {
                    db.updateOnline(username);
                    return "You have successfully login!";
                } else {
                    return "This telegram account has associate with another account!";
                }
            } else {
                return "Please login using the website!";
            }
        }
    }

}
