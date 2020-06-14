public class Item {
    private String name;
    private double price;

    Item(String name, double price) {
        this.name = name;
        this.price = price;
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
        return this.name + " price: " + this.price;
    }
}
