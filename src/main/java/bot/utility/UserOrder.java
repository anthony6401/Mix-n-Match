package bot.utility;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class UserOrder {
    private List<Item> orders;
    private double totalPrice;
    private double deliveryCost;
    private int joinTime;
    private String username;
    private UserStatus status;


    UserOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
        status = UserStatus.NOT_PAID;
    }

    public void resetOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
    }

    public String getUsername() {
        return this.username;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public List<Item> getOrders() {
        return this.orders;
    }

    public double getDeliveryCost() {
        return this.deliveryCost;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public int getJoinTime() {
        return this.joinTime;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setUsername(String username) {
         this.username = username;
    }

    public void setStatusToOrderee() {
        this.status = UserStatus.ORDEREE;
    }

    public void setStatusToPaid() {
        this.status = UserStatus.PAID;
    }

    public void setStatusToNotPaid() {
        this.status = UserStatus.NOT_PAID;
    }

    public void setJoinTime(int joinTime) {
        this.joinTime = joinTime;
    }


    public void addOrder(Item item) {
        this.orders.add(item);
        totalPrice += item.getPrice();
    }

    public boolean deleteOrder(String order) {
        Item toBeRemoved = null;
        for (int i = 0; i < orders.size(); i++) {
            if (StringUtils.containsIgnoreCase(orders.get(i).getName(), order)) {
                toBeRemoved = this.orders.remove(i);
                break;
            }
        }

        if (toBeRemoved == null) {
            return false;
        } else {
            totalPrice -= toBeRemoved.getPrice();
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : orders) {
            sb.append(item.toString() + "\n");
        }

        sb.append("Total Price: $" + String.format("%.2f", this.totalPrice) + "\n" +
                "Total Price : $" + String.format("%.2f", (this.totalPrice + this.deliveryCost))
                + " (Delivery cost included)\n" + "Status: ");

        switch (this.status) {
            case PAID:
                sb.append("Paid");
                break;

            case ORDEREE:
                sb.append("Orderee");
                break;

            default:
                sb.append("Have not paid");
        }

        return sb.toString().trim();
    }
}