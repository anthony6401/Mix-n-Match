package bot.command;

import bot.utility.ClientOrder;
import bot.utility.NoSuchUserExistException;

public class RemoveCommand implements Command {
    private final String orderList;
    private final Integer telegram_id;
    private final ClientOrder co;

    public RemoveCommand(String orderList, Integer telegram_id, ClientOrder co) {
        this.orderList = orderList;
        this.telegram_id = telegram_id;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (co.getExceedTimeLimitStatus()) {
            return "Time's already up! You can't remove items anymore!";
        }

        boolean notFound = false;
        StringBuilder sb = new StringBuilder();
        sb.append("This items can't be found:\n");
        String[] arr = orderList.split("\n");
        for (String item : arr) {
            try {
                if (!co.deleteOrder(telegram_id, item)) {
                    notFound = true;
                    sb.append(item + "\n");
                }
            } catch (NoSuchUserExistException e) {
                return "The user can't be found. Please reset using /logout " +
                        "and login using the link provided in the website!";
            }
        }

        if (notFound) {
            return sb.toString().trim();
        } else {
            return "Successfully removing the orders!";
        }
    }
}
