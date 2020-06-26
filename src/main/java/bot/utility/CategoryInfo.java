package bot.utility;

public class CategoryInfo {
    private final String category;
    private final String URL;

    public CategoryInfo(String category, String URL) {
        this.category = category;
        this.URL = URL;
    }

    public String getCategory() {
        return this.category;
    }

    public String getURL() {
        return this.URL;
    }
}
