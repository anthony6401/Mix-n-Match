package bot.utility;

public class ItemForDB {
    private final String name;
    private final double price;
    private final String desc;
    private final String url;
    private final int restaurant_id;

    public ItemForDB(String name, double price, String desc, String url, int restaurant_id) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.url = url;
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getURL() {
        return this.url;
    }

    public int getRestaurant_id() {
        return this.restaurant_id;
    }
}
