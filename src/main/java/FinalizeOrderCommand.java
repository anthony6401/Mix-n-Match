public class FinalizeOrderCommand implements Command {
    private final String username;
    private final ClientOrder co;

    FinalizeOrderCommand(String username, ClientOrder co) {
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (co.getFinalizeStatus()) {
            return "You have finalize your order! If you want to reset, please make another group!";
        }

        boolean error = false;
        StringBuilder sb = new StringBuilder();
        sb.append("These information are missing:\n");

        if (co.getInviteLink() == null) {
            error = true;
            sb.append("Group invite link. Please use /invitelink [invite link]\n");
        }

        if (co.getFrom() == null) {
            error = true;
            sb.append("From where you want to order. Please use /orderfrom [restaurant name]\n");
        }

        if (co.getTo() == null) {
            error = true;
            sb.append("To where you want to order. Please use /orderto [place name]");
        }

        if (error) {
            return sb.toString();
        }

        co.addUser(username);
        co.setMobileNumber(db.getMobileNumber(username));
        co.finalizeOrder();
        return "Successfully ordering! Notifying all the user around you right now.";
    }
}
