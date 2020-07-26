package bot.command;

import bot.utility.ClientOrder;
import bot.utility.UserStatus;

public class ExitGroupCommand implements Command {
    private final Integer telegram_id;
    private final ClientOrder co;

    public ExitGroupCommand(Integer telegram_id, ClientOrder co) {
        this.telegram_id = telegram_id;
        this.co = co;

    }
    @Override
    public String execute() {
        if (co == null) {
            return "The group that you want to leave from does not exist.";
        }

        if (!co.containsUser(telegram_id)) {
            return "You did not belong to this group!";
        }

        if (co.getUser(telegram_id).getStatus() == UserStatus.ORDEREE) {
            return "You are the orderee. Please don't exit!";
        }

        co.deleteUser(telegram_id);
        return "Successfully leave the group!";
    }
}
