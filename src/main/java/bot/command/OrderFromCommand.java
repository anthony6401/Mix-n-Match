package bot.command;

import bot.utility.ClientOrder;
import bot.utility.Pair;

import java.util.Map;

public class OrderFromCommand implements Command {
    private final Map<Long, ClientOrder> map;
    private final String arg;
    private final long chat_id;

    public OrderFromCommand(Map<Long, ClientOrder> map, String arg, long chat_id) {
        this.map = map;
        this.arg = arg;
        this.chat_id = chat_id;
    }


    @Override
    public String execute() {
        if (arg == null) {
            return "You did not specify where you want to order from!";
        }

        Pair pair =  db.orderFromSearch(arg);
        String restaurant = pair.getFirst();
        String deliveryHours = pair.getSecond();

        if (restaurant.startsWith("No restaurant found")) {
            return restaurant;
        } else {
            if (this.map.containsKey(chat_id)) {
                ClientOrder co = this.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setFrom(restaurant);
                co.setDeliveryHours(deliveryHours);
            } else {
                ClientOrder co = new ClientOrder();
                co.setFrom(restaurant);
                co.setDeliveryHours(deliveryHours);
                this.map.put(chat_id, co);
            }

            return "Ordering from " + restaurant;
        }
    }
}
