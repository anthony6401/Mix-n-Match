package bot.command;

import bot.utility.ClientOrder;

public class CheckOrderStatusCommand implements Command {
    private final ClientOrder co;
    private final long chat_id;

    public CheckOrderStatusCommand(ClientOrder co, long chat_id) {
        this.co = co;
        this.chat_id = chat_id;
    }

    @Override
    public String execute() {
        return co.toString() + "\n\n" +
                "If you want to invite other people that is not around you, they can join by " +
                "typing /join " + chat_id + " in the private chat with the bot.";
    }
}
