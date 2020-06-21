package bot.command;

public class CategoryListCommand implements Command {

    public CategoryListCommand() {
    }

    @Override
    public String execute() {

        return db.getAllCategory();

    }
}
