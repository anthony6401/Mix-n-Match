import java.util.HashMap;
import java.util.Map;

public class ClientOrder {
    private Map<String, UserOrder> userOrderList;
    private String from;
    private String to;
    private String inviteLink;
    private Integer timeLimit; // In seconds
    private Integer startTime;
    private double deliveryCost;
    public int numberOfPeople;
    private boolean finalizeStatus;
    private String mobileNumber;

    ClientOrder() {
        this.from = null;
        this.to = null;
        this.userOrderList = new HashMap();
        this.numberOfPeople = 0;
        this.finalizeStatus = false;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() { return this.to; };

    public boolean getFinalizeStatus() { return this.finalizeStatus; };

    public String getInviteLink() {return this.inviteLink; };

    public Integer getTimeLimit() {
        return this.timeLimit;
    }

    public Integer getStartTime() {
        return this.startTime;
    }

    public ClientOrder setFrom(String from) {
        this.from = from;
        deleteAllOrder();
        return this;
    }

    public ClientOrder setTo(String to) {
        this.to = to;
        return this;
    }

    public ClientOrder finalizeOrder() {
        this.finalizeStatus = true;
        return this;
    }

    public ClientOrder setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
        return this;
    }

    public ClientOrder setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public ClientOrder setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public ClientOrder setStartTime(Integer startTime) {
        this.startTime = startTime;
        return this;
    }

    public ClientOrder setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
        return this;
    }

    public boolean isWithinTimeLimit(long time) {
        return time - startTime <= timeLimit;
    }

    public ClientOrder addUser(String username) {
        numberOfPeople++;
        userOrderList.put(username, new UserOrder());
        return this;
    }

    public boolean containsUser(String username) {
        return userOrderList.containsKey(username);
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

    public ClientOrder deleteAllOrderFromUser(String username) throws NoSuchUserExistException {
        try {
            UserOrder uo = userOrderList.get(username);
            uo.resetOrder();
            return this;
        } catch (NullPointerException e) {
            throw new NoSuchUserExistException();
        }
    }

    public boolean deleteOrder(String username, String order) throws NoSuchUserExistException {
        try {
            UserOrder uo = userOrderList.get(username);
            return uo.deleteOrder(order);
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
            entry.getValue().setDeliveryCost(deliveryCost / numberOfPeople);
            sb.append(entry.getValue().toString() + "\n\n");
        }

        if (inviteLink != null) {
            sb.append("Group invite link: " + inviteLink + "\n");
        }

        if (mobileNumber != null) {
            sb.append("Pay to: " + mobileNumber + "\n");
        }

        if (startTime != null) {
            sb.append("End at: " + DateTime.unixTimeToDate(startTime + timeLimit));
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