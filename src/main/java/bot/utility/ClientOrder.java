package bot.utility;

import java.util.HashMap;
import java.util.Map;

public class ClientOrder extends HashMap<Integer, UserOrder> {
    private String from;
    private String to;
    private String inviteLink;
    private Integer timeLimit; // In seconds
    private Integer startTime;
    private boolean exceedTimeLimit;
    private double deliveryCost;
    public int numberOfPeople;
    private boolean finalizeStatus;
    private String mobileNumber;

    public ClientOrder() {
        this.from = null;
        this.to = null;
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

    public boolean getExceedTimeLimitStatus() { return this.exceedTimeLimit; };

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

    public ClientOrder setExceedTimeLimitToTrue() {
        this.exceedTimeLimit = true;
        return this;
    }

    public ClientOrder finalizeOrder() {
        this.finalizeStatus = true;
        return this;
    }

    public UserOrder getUser(Integer telegram_id) {
        return get(telegram_id);
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

    public ClientOrder addUser(Integer telegram_id, String username) {
        numberOfPeople++;
        UserOrder uo = new UserOrder();
        uo.setUsername(username);
        put(telegram_id, uo);
        return this;
    }

    public ClientOrder deleteUser(Integer telegram_id) {
        numberOfPeople--;
        remove(telegram_id);
        return this;
    }

    public boolean containsUser(Integer telegram_id) {
        return containsKey(telegram_id);
    }

    public ClientOrder addOrder(Integer telegram_id, Item item) throws NoSuchUserExistException {
        try {
            UserOrder uo = get(telegram_id);
            uo.addOrder(item);
            return this;
        } catch (NullPointerException e) {
            throw new NoSuchUserExistException();
        }
    }

    public ClientOrder deleteAllOrder() {
        for (Map.Entry<Integer, UserOrder> entry : entrySet()) {
            entry.getValue().resetOrder();
        }
        return this;
    }

    public ClientOrder deleteAllOrderFromUser(Integer telegram_id) throws NoSuchUserExistException {
        try {
            UserOrder uo = get(telegram_id);
            uo.resetOrder();
            return this;
        } catch (NullPointerException e) {
            throw new NoSuchUserExistException();
        }
    }

    public boolean deleteOrder(Integer telegram_id, String order) throws NoSuchUserExistException {
        try {
            UserOrder uo = get(telegram_id);
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
        for (Map.Entry<Integer, UserOrder> entry : entrySet()) {
            UserOrder uo = entry.getValue();
            sb.append(uo.getUsername() + "'s orders:\n");
            uo.setDeliveryCost(deliveryCost / numberOfPeople);
            sb.append(uo.toString() + "\n\n");
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