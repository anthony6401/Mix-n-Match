package bot.command;

import bot.utility.ClientOrder;
import bot.utility.DateTime;
import bot.utility.UserOrder;
import bot.utility.UserStatus;


import java.util.Map;

public class PaymentTimeLimitExceedCommand implements Command {
    private final ClientOrder co;
    private final long order_id;
    private final int ONE_MINUTE = 60;

    public PaymentTimeLimitExceedCommand(long order_id, ClientOrder co) {
        this.co = co;
        this.order_id = order_id;
    }

    @Override
    public String execute() {
        for (Map.Entry<Integer, UserOrder> entry : co.entrySet()) {
            int telegram_id = entry.getKey();
            UserOrder uo = entry.getValue();

            if (uo.getStatus() == UserStatus.PAID || uo.getStatus() == UserStatus.ORDEREE) {
                if (!uo.getOrders().isEmpty()) {
                    db.addFoodHistory(order_id, telegram_id, uo);
                    db.addHistory(DateTime.unixTimeToDate(uo.getJoinTime()), uo.getDeliveryCost(), telegram_id, co.getFrom(), co.getTo(), order_id);
                }
            } else {
                int time = co.getStartTime() + co.getOrderTimeLimit() + co.getPaymentTimeLimit() + ONE_MINUTE;
                db.addBanned(telegram_id, time);
            }
        }

        return "Time's up! Thank you for using the bot!\n\n" +
                "Here is the order status:\n" + co.toString();
    }
}
