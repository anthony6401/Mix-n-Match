package bot.command;

import java.util.Map;
import bot.utility.ClientOrder;
import bot.utility.DateTime;

public class PaymentTimeCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String timeLimit; // In minutes
    private final long chat_id;


    public PaymentTimeCommand(Map<Long, ClientOrder> map, String timeLimit, long chat_id) {
        this.map = map;
        this.timeLimit = timeLimit;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (timeLimit == null) {
            return "You did not specify any payment time limit. Please type it in the format /paymenttime [time limit]!";
        }

        try {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setPaymentTimeLimit(Integer.valueOf(timeLimit) * DateTime.MINUTES_TO_SECONDS); // Change into seconds
            } else {
                ClientOrder co = new ClientOrder();
                co.setPaymentTimeLimit(Integer.valueOf(timeLimit) * DateTime.MINUTES_TO_SECONDS); // Change into seconds
                this.map.put(chat_id, co);
            }
        } catch (NumberFormatException e) {
            return "Invalid time! Please don't use \".\" and \",\"!";
        }

        return "Successfully adding the payment time limit!";
    }
}
