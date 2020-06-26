package bot.command;

import bot.utility.ClientOrder;

public class PaymentTimeLimitExceedCommand implements Command {
    private final ClientOrder co;

    public PaymentTimeLimitExceedCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        return "Time's up! Thank you for using the bot!\n\n" +
                "Here is the order status:\n" + co.toString();
    }
}
