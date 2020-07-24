package bot.utility;

import bot.command.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Notification {
    private Integer telegram_id;
    private long chat_id;
    private ClientOrder co;
    private final double RADIUS_NOTIFICATION = 0.5;

    public Notification(long chat_id, Integer telegram_id, ClientOrder co) {
        this.telegram_id = telegram_id;
        this.chat_id = chat_id;
        this.co = co;
    }

    public Notification(ClientOrder co) {
        this.co = co;
    }

    public List<SendMessage> notifyUser() {
        String botMessage = "Someone is ordering from " + co.getFrom() + " to " + co.getTo() + ".\n" +
                "To join the group, press the button below\n" +
                "The group order ended at " +
                DateTime.unixTimeToDate(co.getStartTime() + co.getOrderTimeLimit()) + "\n" +
                "The group payment ended at " +
                DateTime.unixTimeToDate(co.getStartTime() + co.getOrderTimeLimit() + co.getPaymentTimeLimit());

            List<SendMessage> result = new ArrayList<>();

        List<UserInfo> userInfoList = Command.db.getListOfOnlineUserExceptUser(telegram_id);
        PlaceInfo pi = Command.googleMap.getPlaceCoordinates(co.getTo());
        double userLongitude = pi.getLongitude();
        double userLatitude = pi.getLatitude();

        for (UserInfo ui : userInfoList) {
            double longitude = ui.getLongitude();
            double latitude = ui.getLatitude();

            if (isWithinRadius(userLatitude, userLongitude, latitude, longitude)) {
                SendMessage message = new SendMessage();
                message.setChatId(ui.getChatID());
                message.setText(botMessage);
                result.add(message);

                InlineKeyboardMarkup inkMarkup = new InlineKeyboardMarkup();
                InlineKeyboardButton ikButton = new InlineKeyboardButton();
                List<List<InlineKeyboardButton>> list = new ArrayList<>();
                List<InlineKeyboardButton> ikButtonList = new ArrayList<>();
                ikButton.setText("join");
                ikButton.setCallbackData("/join " + chat_id);
                ikButtonList.add(ikButton);
                list.add(ikButtonList);
                inkMarkup.setKeyboard(list);
                message.setReplyMarkup(inkMarkup);
            }
        }

        // Test by sending the notification to myself
        SendMessage message = new SendMessage();
        message.setChatId((long) 861353631);
        message.setText(botMessage);
        result.add(message);

        InlineKeyboardMarkup inkMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton ikButton = new InlineKeyboardButton();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> ikButtonList = new ArrayList<>();
        ikButton.setText("join");
        ikButton.setCallbackData("/join " + chat_id);
        ikButtonList.add(ikButton);
        list.add(ikButtonList);
        inkMarkup.setKeyboard(list);
        message.setReplyMarkup(inkMarkup);

        return result;
    }

    public List<SendMessage> notifyUserForOrderTimesUp() {

        List<SendMessage> result = new ArrayList<>();

        for (Map.Entry<Integer, UserOrder> entry : co.entrySet()) {
            SendMessage message = new SendMessage();
            message.setChatId((long) entry.getKey());
            UserOrder uo = entry.getValue();
            StringBuffer sb = new StringBuffer();
            sb.append("Here is your order detail!\n");

            for (Item item : uo.getOrders()) {
                sb.append(item.toString() + "\n");
            }
            sb.append("Delivery cost: $" + uo.getDeliveryCost() + "\n");
            sb.append("Please pay $"
                    + (uo.getTotalPrice() + uo.getDeliveryCost()) + " to " + co.getMobileNumber());
            String botMessage = sb.toString();
            message.setText(botMessage);
            result.add(message);
        }

        return result;
    }

    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2) {
        int r = 6371; //earth radius
        double dLat = degToRad(lat2 - lat1);
        double dLon = degToRad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c; // Distance in km
        return d <= RADIUS_NOTIFICATION;
    }

    private double degToRad(double deg) {
        return deg * (Math.PI / 180);
    }

}