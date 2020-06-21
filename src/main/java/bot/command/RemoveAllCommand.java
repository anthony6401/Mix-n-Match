package bot.command;

import bot.utility.ClientOrder;
import bot.utility.NoSuchUserExistException;

public class RemoveAllCommand implements Command {
    private final Integer telegram_id;
    private final ClientOrder co;

    public RemoveAllCommand(Integer telegram_id, ClientOrder co) {
        this.telegram_id = telegram_id;
        this.co = co;
    }

    @Override
    public String execute() {
        try {
            co.deleteAllOrderFromUser(telegram_id);
            return "Successfully removing all the orders!";
        } catch (NoSuchUserExistException e) {
            return "The user can't be found. Please reset using /logout " +
                    "and login using the link provided in the website!";
        }
    }
}
