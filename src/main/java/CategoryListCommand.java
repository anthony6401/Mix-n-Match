public class CategoryListCommand implements Command {
    private final String page;

    CategoryListCommand(String page) {
        this.page = page;
    }

    @Override
    public String execute() {
        if (page == null) {
            return db.getAllCategory(1);
        } else {
            int intPage = Integer.valueOf(page);
            return db.getAllCategory(intPage);
        }
    }
}
