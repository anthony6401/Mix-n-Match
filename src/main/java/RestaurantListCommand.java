public class RestaurantListCommand implements Command {
    private final String category;

    RestaurantListCommand(String category) {
        this.category = category;
    }

    @Override
    public String execute() {
        return db.getRestaurantList(category);
    }
}
