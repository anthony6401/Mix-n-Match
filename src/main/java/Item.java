public class Item {
    private String name;
    private double price;
    private String desc;

    Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.desc = null;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Item) {
            Item otherItem = (Item) other;
            return this.name.equals(otherItem.name);
        }

        return false;
    }

    @Override
    public String toString() {
        if (desc == null) {
            return this.name + " price: " + this.price;
        }
        return this.name + " price: " + this.price + " -- " + this.desc;
    }
}
