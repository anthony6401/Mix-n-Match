public class OrderFromCommand implements Command {
    private final String arg;
    private final long chat_id;

    OrderFromCommand(String arg, long chat_id) {
        this.arg = arg;
        this.chat_id = chat_id;
    }


    @Override
    public String execute() {
        if (arg == null) {
            return "You did not specify where you want to order from!";
        }

        String restaurant =  db.orderFromSearch(arg);

        if (restaurant.startsWith("No restaurant found")) {
            return restaurant;
        } else {
            if (MixnMatchBot.map.containsKey(chat_id)) {
                ClientOrder co = MixnMatchBot.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setFrom(restaurant);
            } else {
                ClientOrder co = new ClientOrder();
                co.setFrom(restaurant);
                MixnMatchBot.map.put(chat_id, co);
            }

            return "Ordering from " + restaurant;
        }
    }
}
