package bot.command;

public class MenuCommand implements Command {
    private final String restaurant;

    public MenuCommand(String restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String execute() {
        return db.getRestaurantMenu(restaurant);
    }
}
