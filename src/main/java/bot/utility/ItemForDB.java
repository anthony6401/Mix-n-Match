package bot.utility;

public class ItemForDB {
    private String name;
    private double price;
    private String desc;
    private String url;

    public ItemForDB(String name, double price, String desc, String url) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.url = url;
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
}
