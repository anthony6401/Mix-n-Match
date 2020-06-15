import java.util.HashMap;
import java.util.Map;

public class ClientOrder {
    private Map<String, UserOrder> userOrderList;
    private String from;
    private String to;
    private String inviteLink;
    private String payNumber;

    ClientOrder() {
        this.from = null;
        this.to = null;
        this.userOrderList = new HashMap();
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() { return this.to; };

    public ClientOrder setFrom(String from) {
        this.from = from;
        deleteAllOrder();
        return this;
    }

    public ClientOrder setTo(String to) {
        this.to = to;
        return this;
    }

    public ClientOrder setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
        return this;
    }

    public ClientOrder setPayNumber(String payNumber) {
        this.payNumber = payNumber;
        return this;
    }

    public ClientOrder addUser(String username) {
        userOrderList.put(username, new UserOrder());
        return this;
    }

    public ClientOrder addOrder(String username, Item item) throws NoSuchUserExistException {
        try {
            UserOrder uo = userOrderList.get(username);
            uo.addOrder(item);
            return this;
        } catch (NullPointerException e) {
            throw new NoSuchUserExistException();
        }
    }

    public ClientOrder deleteAllOrder() {
        for (Map.Entry<String, UserOrder> entry : userOrderList.entrySet()) {
            entry.setValue(new UserOrder());
        }
        return this;
    }

    public ClientOrder deleteOrder(String username, String order) throws NoSuchUserExistException {
        try {
            UserOrder uo = userOrderList.get(username);
            uo.deleteOrder(order);
            return this;
        } catch (NullPointerException e) {
            throw new NoSuchUserExistException();
        }
    }

    @Override
    public String toString() {
        if (from == null && to == null) {
            return "No order from users yet.";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, UserOrder> entry : userOrderList.entrySet()) {
            sb.append(entry.getKey() + "'s orders:\n");
            sb.append(entry.getValue().toString() + "\n");
        }

        if (from == null) {
            return "Ordering to " + this.to + "\n" + sb.toString().trim();
        } else if (to == null) {
            return "Ordering from " + this.from + "\n" + sb.toString().trim();
        } else {
            return "Ordering from " + this.from + " to " + this.to + "\n" + sb.toString().trim();
        }
    }

}