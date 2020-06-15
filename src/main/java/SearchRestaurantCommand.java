public class SearchRestaurantCommand implements Command {
    private final String restaurantKeyword;

    SearchRestaurantCommand(String restaurantKeyword) {
        this.restaurantKeyword = restaurantKeyword;
    }

    @Override
    public String execute() {
        return db.searchRestaurant(restaurantKeyword);
    }
}
