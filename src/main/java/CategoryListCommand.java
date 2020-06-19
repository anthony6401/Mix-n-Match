public class CategoryListCommand implements Command {

    CategoryListCommand() {
    }

    @Override
    public String execute() {

        return db.getAllCategory();

    }
}
