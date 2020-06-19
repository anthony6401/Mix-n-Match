import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class Notification {
    private final String username;
    private final long chat_id;
    private final ClientOrder co;
    private final double RADIUS_NOTIFICATION = 0.5;

    Notification(String username, long chat_id, ClientOrder co) {
        this.username = username;
        this.chat_id = chat_id;
        this.co = co;
    }

    public List<SendMessage> notifyUser() {
        String botMessage = "Someone is ordering from " + co.getFrom() + " to " + co.getTo() + ".\n" +
                "To join the group, please type /join " + chat_id + "\n" +
                "The group order ended at " + DateTime.unixTimeToDate(co.getStartTime() + co.getTimeLimit());

        List<SendMessage> result = new ArrayList<>();

        List<UserInfo> userInfoList = Command.db.getListOfOnlineUserExceptUser(username);
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
            }
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

    public static void main(String[] args) {
        Notification notif = new Notification("abc", 123, new ClientOrder());
        PlaceInfo pi = Command.googleMap.getPlaceCoordinates("smak 4 penabur");
        System.out.println(pi.getLatitude());
        System.out.println(notif.isWithinRadius(-6.17494, 106.763278, -6.174105, 106.763602));

    }

}