package bot.command;

import bot.utility.ClientOrder;
import bot.utility.DateTime;

public class JoinCommand implements Command {
    private final String username;
    private final Integer telegram_id;
    private final Integer chatTime;
    private final ClientOrder co;

    public JoinCommand(String username, Integer telegram_id, Integer chatTime, ClientOrder co) {
        this.username = username;
        this.telegram_id = telegram_id;
        this.chatTime = chatTime;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "The group that you trying to join is not available anymore.";
        }

        if (!co.isWithinTimeLimit(chatTime)) {
            return "The time limit has passed!";
        }

        if (co.containsUser(telegram_id)) {
            return "You have join this group!";
        }

        co.addUser(telegram_id, username);
        db.addHistory(DateTime.unixTimeToDate(co.getStartTime()), telegram_id, co.getFrom(), co.getTo());
        return "Successfully join a group!\n" +
                "Here is the group invite link: " + co.getInviteLink();

    }
}
