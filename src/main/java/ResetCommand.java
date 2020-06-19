public class ResetCommand implements Command {
    private final Long chat_id;

    ResetCommand(long chat_id) {
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        if (chat_id == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (MixnMatchBot.map.get(chat_id).getFinalizeStatus()) {
            return "You have finalize your order! If you want to reset, please make another group!";
        }

        MixnMatchBot.map.put(chat_id, new ClientOrder());
        return "Successfully clear the order!";
    }
}
