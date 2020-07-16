package bot.utility;

public class CategoryForDB {
    private final String category;
    private final int category_id;

    public CategoryForDB(String category, int category_id) {
        this.category = category;
        this.category_id = category_id;
    }

    public String getCategory() {
        return this.category;
    }

    public int getCategory_id() {
        return this.category_id;
    }
}
