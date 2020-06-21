import web.extract.Extract;

public class WebExtraction {
    public static void main(String[] args) {
        final long startTime = System.nanoTime();
        String foodPandaURL = "https://www.foodpanda.sg/";
        Extract ex = new Extract();
        ex.extract(foodPandaURL);
        final long duration = System.nanoTime() - startTime;
        System.out.println("Duration: " + duration/1000000000F);
    }
}
