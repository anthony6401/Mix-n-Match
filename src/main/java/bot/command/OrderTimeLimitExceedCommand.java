package bot.command;

import bot.utility.ClientOrder;
import bot.utility.Notification;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class OrderTimeLimitExceedCommand implements Command {
    private final ClientOrder co;

    public OrderTimeLimitExceedCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        return "Time's up! Please proceed to the payment!\n" +
                "Here is the phone number to pay to: " + co.getMobileNumber();
    }
}
