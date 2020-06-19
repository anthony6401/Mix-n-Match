import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

public class UserOrder {
    private List<Item> orders;
    private double totalPrice;
    private double deliveryCost;

    UserOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
    }

    public void resetOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
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

        sb.append("Total Price: $" + this.totalPrice + "\n" +
                "Total Price : $" + String.format("%.2f", (this.totalPrice + this.deliveryCost))
                + " (Delivery cost included)");

        return sb.toString().trim();
    }
}