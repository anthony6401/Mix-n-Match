package bot.command;

import bot.utility.ClientOrder;

import java.util.Map;

public class OrderToCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String arg;
    private final long chat_id;

    public OrderToCommand(Map<Long, ClientOrder> map, String arg, long chat_id) {
        this.map = map;
        this.arg = arg;
        this.chat_id = chat_id;
    }


    @Override
    public String execute() {
        if (arg == null) {
            return "You did not specify where you want to order to!";
        }


        String place = googleMap.findPlace(arg);

        if (place.startsWith("No place found")) {
            return place;
        } else {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setTo(place);
            } else {
                ClientOrder co = new ClientOrder();
                co.setTo(place);
                this.map.put(chat_id, co);
            }

            return "Ordering to " + place;
        }
    }


}
