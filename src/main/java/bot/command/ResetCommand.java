package bot.command;

import bot.utility.ClientOrder;

import java.util.Map;

public class ResetCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final Long chat_id;

    public ResetCommand(Map<Long, ClientOrder> map, long chat_id) {
        this.map = map;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (chat_id == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (this.map.get(chat_id).getFinalizeStatus()) {
            return "You have finalize your order! If you want to reset, please make another group!";
        }

        this.map.put(chat_id, new ClientOrder());
        return "Successfully clear the order!";
    }
}
