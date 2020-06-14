import java.util.List;
import java.util.ArrayList;

public class UserOrder {
    private List<Item> orders;
    private double totalPrice;

    UserOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
    }

    public void resetOrder() {
        this.orders = new ArrayList<>();
        totalPrice = 0;
    }

    public void addOrder(Item item) {
        this.orders.add(item);
        totalPrice += item.getPrice();
    }

    public void deleteOrder(String order) {
        Item toBeRemoved = null;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getName().equals(order)) {
                toBeRemoved = this.orders.remove(i);
            }
        }
        totalPrice -= toBeRemoved.getPrice();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : orders) {
            System.out.println("In UserOrder" + item);
            sb.append(item.toString() + "\n");
        }

        sb.append("TotalPrice: $" + this.totalPrice + "\n");

        return sb.toString().trim();
    }
}