public class OrderToCommand implements Command {
    private final String arg;
    private final long chat_id;

    OrderToCommand(String arg, long chat_id) {
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
            if (MixnMatchBot.map.containsKey(chat_id)) {
                ClientOrder co = MixnMatchBot.map.get(chat_id);
                co.setTo(place);
            } else {
                ClientOrder co = new ClientOrder();
                co.setTo(place);
                MixnMatchBot.map.put(chat_id, co);
            }

            return "Ordering to " + place;
        }
    }


}
