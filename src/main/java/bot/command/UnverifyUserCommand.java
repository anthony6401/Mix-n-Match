package bot.command;

import bot.utility.ClientOrder;
import bot.utility.UserOrder;
import bot.utility.UserStatus;

import java.util.Map;

public class UnverifyUserCommand implements Command {
    public final String username;
    public final Integer telegram_id;
    public final ClientOrder co;

    public UnverifyUserCommand(String username, Integer telegram_id, ClientOrder co) {
        this.username = username;
        this.telegram_id = telegram_id;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (!co.getExceedTimeLimitStatus()) {
            return "This is still in the order period! You can only verify on payment period.";
        }

        if (co.getExceedPaymentTimeLimitStatus()) {
            return "Time's already up! You can't unverify other people's payment anymore!";
        }

        if (co.getUser(telegram_id).getStatus() == UserStatus.ORDEREE) {
            for (Map.Entry<Integer, UserOrder> entry : co.entrySet()) {
                if (entry.getValue().getUsername().equals(username) ||
                        entry.getValue().getUsername().equals("@" + username)) {
                    co.get(telegram_id).setStatusToNotPaid();
                    return "Successfully unverifying the user's payment!";
                }
            }
            return "The username that you input is invalid!";
        } else {
            return "You are not the orderee! You can't verify other people's payment.";
        }
    }
}