package bot.command;

import bot.command.Command;

public class RestaurantListCommand implements Command {
    private final String category;

    public RestaurantListCommand(String category) {
        this.category = category;
    }

    @Override
    public String execute() {
        return db.getRestaurantList(category);
    }
}
