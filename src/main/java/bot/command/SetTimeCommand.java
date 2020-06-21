package bot.command;

import bot.utility.ClientOrder;

import java.util.Map;

public class SetTimeCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String timeLimit; // In minutes
    private final long chat_id;
    private final int MINUTES_TO_SECONDS = 60;


    public SetTimeCommand(Map<Long, ClientOrder> map, String timeLimit, long chat_id) {
        this.map = map;
        this.timeLimit = timeLimit;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (timeLimit == null) {
            return "You did not specify any time limit. Please type it in the format /settime [time limit]!";
        }

        try {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setTimeLimit(Integer.valueOf(timeLimit) * MINUTES_TO_SECONDS); // Change into seconds
            } else {
                ClientOrder co = new ClientOrder();
                co.setTimeLimit(Integer.valueOf(timeLimit) * MINUTES_TO_SECONDS); // Change into seconds
                this.map.put(chat_id, co);
            }
        } catch (NumberFormatException e) {
            return "Invalid time! Please don't use \".\" and \",\"!";
        }

        return "Successfully adding the time limit!";
    }
}
