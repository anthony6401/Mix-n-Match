package bot.command;

import bot.utility.ClientOrder;

public class PaymentTimeLimitExceedCommand implements Command {

    @Override
    public String execute() {
        return "Time's up! Thank you for using the bot!";
    }
}
