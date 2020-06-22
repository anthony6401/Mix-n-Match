package bot.command;

import bot.utility.ClientOrder;
import bot.utility.DateTime;
import bot.utility.Notification;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class FinalizeOrderCommand implements Command {
    private final String username;
    private final Integer telegram_id;
    private final long chat_id;
    public final Integer startTime;
    private final ClientOrder co;

    public FinalizeOrderCommand(String username, Integer telegram_id,
                                long chat_id, Integer startTime, ClientOrder co) {
        this.username = username;
        this.telegram_id = telegram_id;
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

        if (co.getOrderTimeLimit() == null) {
            error = true;
            sb.append("The order time limit of the order. Please use /ordertime [time limit]");
        }

        if (co.getPaymentTimeLimit() == null) {
            error = true;
            sb.append("The payment time limit of the order. Please use /paymenttime [time limit]");
        }

        if (error) {
            return sb.toString();
        }

        db.addHistory(DateTime.unixTimeToDate(startTime), telegram_id, co.getFrom(), co.getTo());
        co.addUser(telegram_id, username);
        co.setMobileNumber(db.getMobileNumber(telegram_id));
        co.setStartTime(startTime);
        co.getUser(telegram_id).setStatusToOrderee();
        co.finalizeOrder();
        return "Successfully ordering! Notifying all the user around you right now.";
    }

    public List<SendMessage> notifyOnlineUser() {
        Notification notification = new Notification(chat_id, telegram_id, co);
        return notification.notifyUser();
    }
}
