package bot.command;

import bot.utility.ClientOrder;

public class DeliveryCostCommand implements Command {
    private final String deliveryCost;
    private final ClientOrder co;

    public DeliveryCostCommand(String deliveryCost, ClientOrder co) {
        this.deliveryCost = deliveryCost;
        this.co = co;
    }

    @Override
    public String execute() {
        if (deliveryCost == null) {
            return "You did not specify any time limit. " +
                    "Please type it in the format /deliverycost [delivery cost]!";
        }

        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        co.setDeliveryCost(Double.valueOf(deliveryCost));
        return "Successfully adding the delivery cost!";
    }
}
