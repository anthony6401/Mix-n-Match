import bot.utility.DatabaseCon;
import web.extract.Extract;

public class WebExtraction {
    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        DatabaseCon db = new DatabaseCon();
        db.initializeRestaurantDatabase(); // Reset the database
        String foodPandaURL = "https://www.foodpanda.sg/"; // URL from where the menu will be taken
        Extract ex = new Extract();
        ex.extract(foodPandaURL); // Extract the menu from the website
        final long duration = System.nanoTime() - startTime;
        System.out.println("Duration: " + duration/1000000000F);
    }
}
