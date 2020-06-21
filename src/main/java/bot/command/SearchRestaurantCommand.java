package bot.command;

public class SearchRestaurantCommand implements Command {
    private final String restaurantKeyword;

    public SearchRestaurantCommand(String restaurantKeyword) {
        this.restaurantKeyword = restaurantKeyword;
    }

    @Override
    public String execute() {
        return db.searchRestaurant(restaurantKeyword);
    }
}
