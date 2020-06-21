package bot.command;

import bot.utility.ClientOrder;

public class OrdereeCommand implements Command {
    private final String username;
    private final ClientOrder co;

    public OrdereeCommand(String username, ClientOrder co) {
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        return null;
    }
}
