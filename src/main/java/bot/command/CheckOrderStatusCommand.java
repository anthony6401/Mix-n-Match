package bot.command;

import bot.utility.ClientOrder;

public class CheckOrderStatusCommand implements Command {
    private ClientOrder co;

    public CheckOrderStatusCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        return co.toString();
    }
}
