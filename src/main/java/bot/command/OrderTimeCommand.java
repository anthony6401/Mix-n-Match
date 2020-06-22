package bot.command;

import bot.utility.ClientOrder;
import bot.utility.DateTime;

import java.util.Map;

public class OrderTimeCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String timeLimit; // In minutes
    private final long chat_id;


    public OrderTimeCommand(Map<Long, ClientOrder> map, String timeLimit, long chat_id) {
        this.map = map;
        this.timeLimit = timeLimit;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (timeLimit == null) {
            return "You did not specify any order time limit. Please type it in the format /ordertime [time limit]!";
        }

        try {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setOrderTimeLimit(Integer.valueOf(timeLimit) * DateTime.MINUTES_TO_SECONDS); // Change into seconds
            } else {
                ClientOrder co = new ClientOrder();
                co.setOrderTimeLimit(Integer.valueOf(timeLimit) * DateTime.MINUTES_TO_SECONDS); // Change into seconds
                this.map.put(chat_id, co);
            }
        } catch (NumberFormatException e) {
            return "Invalid time! Please don't use \".\" and \",\"!";
        }

        return "Successfully adding the order time limit!";
    }
}
