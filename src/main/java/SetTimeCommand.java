public class SetTimeCommand implements Command {
    private final String timeLimit; // In minutes
    private final long chat_id;


    SetTimeCommand(String timeLimit, long chat_id) {
        this.timeLimit = timeLimit;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (timeLimit == null) {
            return "You did not specify any time limit. Please type it in the format /settime [time limit]!";
        }

        if (MixnMatchBot.map.get(chat_id).getFinalizeStatus()) {
            return "You have finalize your order! If you want to reset, please make another group!";
        }

        try {
            if (MixnMatchBot.map.containsKey(chat_id)) {
                ClientOrder co = MixnMatchBot.map.get(chat_id);
                if (co.getFinalizeStatus()) {
                    return "You have finalize your order! If you want to reset, please make another group!";
                }
                co.setTimeLimit(Integer.valueOf(timeLimit) * 60); // Change into seconds
            } else {
                ClientOrder co = new ClientOrder();
                co.setTimeLimit(Integer.valueOf(timeLimit) * 60); // Change into seconds
                MixnMatchBot.map.put(chat_id, co);
            }
        } catch (NumberFormatException e) {
            return "Invalid time! Please don't use \".\" and \",\"!";
        }

        return "Successfully adding the time limit!";
    }
}
