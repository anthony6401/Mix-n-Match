import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class FinalizeOrderCommand implements Command {
    private final String username;
    private final long chat_id;
    public final Integer startTime;
    private final ClientOrder co;

    FinalizeOrderCommand(String username, long chat_id, Integer startTime, ClientOrder co) {
        this.username = username;
        this.chat_id = chat_id;
        this.startTime = startTime;
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
            sb.append("To where you want to order. Please use /orderto [place name]\n");
        }

        if (co.getTimeLimit() == null) {
            error = true;
            sb.append("The time limit of the order. Please use /settime [time limit]");
        }

        if (error) {
            return sb.toString();
        }

        co.addUser(username);
        co.setMobileNumber(db.getMobileNumber(username));
        co.setStartTime(startTime);
        co.finalizeOrder();
        return "Successfully ordering! Notifying all the user around you right now.";
    }

    public List<SendMessage> notifyOnlineUser() {
        Notification notification = new Notification(username, chat_id, co);
        return notification.notifyUser();
    }
}
