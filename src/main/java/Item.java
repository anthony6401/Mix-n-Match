public class Item {
    private String name;
    private float price;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return this.name + " price: " + this.price;
    }
}
