package bot.utility;

public class RestaurantInfo {
    private final String restaurantName;
    private final String URL;
    private final String deliveryHours;

    public RestaurantInfo(String name, String URL, String deliveryHours) {
        this.restaurantName = name;
        this.URL = URL;
        this.deliveryHours = deliveryHours;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public String getURL() {
        return this.URL;
    }

    public String getDeliveryHours() {
        return this.deliveryHours;
    }
}
