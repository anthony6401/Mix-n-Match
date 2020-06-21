package bot.utility;

public class Pair {
    private String name;
    private String URL;

    public Pair(String name, String URL) {
        this.name = name;
        this.URL = URL;
    }

    public String getName() {
        return this.name;
    }

    public String getURL() {
        return this.URL;
    }
}
