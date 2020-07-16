package bot.utility;

public class RestaurantInfo {
    private final String restaurantName;
    private final String URL;
    private final String deliveryHours;
    private final int category_id;

    public RestaurantInfo(String name, String URL, String deliveryHours, int category_id) {
        this.restaurantName = name;
        this.URL = URL;
        this.deliveryHours = deliveryHours;
        this.category_id = category_id;
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

    public int getCategory_id() {
        return this.category_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RestaurantInfo) {
            RestaurantInfo object = (RestaurantInfo) obj;
            return object.restaurantName == this.restaurantName;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return restaurantName.hashCode();
    }
}
