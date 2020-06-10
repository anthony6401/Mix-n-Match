import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Extract {

    public List<Item> getFoodInfo(String url) {
        List<Item> result = new ArrayList<>();
        WebClient client = new WebClient();

        client.setJavaScriptEnabled(false);
        client.setCssEnabled(false);

        try {
            client.setUseInsecureSSL(true);
            HtmlPage page = client.getPage(url);
            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//li[contains(@class, 'dish-card')]");

            if (items.isEmpty()) {
                System.out.println("No items found");
            } else {
                for (HtmlElement element : items) {
                    HtmlSpan name = element.getFirstByXPath("div/div/div/h3/span");
                    HtmlSpan price = element.getFirstByXPath("div/section/div/span");
                    String sPrice = price.asText();

                    Item item = new Item();
                    item.setName(name.asText());
                    item.setPrice(Float.valueOf(sPrice.substring(sPrice.length() - 4)));

                    result.add(item);
                }
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Pair> getRestaurantInfo(String url) {
        List<Pair> result = new ArrayList<>();
        WebClient client = new WebClient();

        client.setJavaScriptEnabled(false);
        client.setCssEnabled(false);

        try {
            client.setUseInsecureSSL(true);
            HtmlPage page = client.getPage(url);
            List<HtmlElement> URLs = (List<HtmlElement>) page.getByXPath("//li/a");

            for (HtmlElement element : URLs) {
                String URL = element.getAttribute("href");
                HtmlSpan span = element.getFirstByXPath("figure/figcaption/span/span");

                if (URL.contains("/chain/") || URL.contains("/restaurant/")) {
                    String name = span.asText().replaceAll("\\(.*\\)", "");
                    if (URL.startsWith("http")) {
                        result.add(new Pair(name, URL));
                    } else {
                        result.add(new Pair(name, "https://www.foodpanda.sg" + URL));
                    }
                }
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Pair> getCategory(String url) {
        List<Pair> result = new ArrayList<>();
        WebClient client = new WebClient();

        client.setJavaScriptEnabled(false);
        client.setCssEnabled(false);

        try {
            client.setUseInsecureSSL(true);
            HtmlPage page = client.getPage(url);
            //getting all the URLs category from FoodPanda
            List<HtmlElement> categories = (List<HtmlElement>) page.getByXPath("//div[contains(@class, " +
                    "'home-cuisines')]");
            //getting all the categories "under more deliveries near you"
            List<HtmlElement> URLs = (List<HtmlElement>) categories.get(0).getByXPath("ul/li");

            for (HtmlElement element : URLs) {
                HtmlAnchor anchor = element.getFirstByXPath("a");
                String URL = anchor.getAttribute("href");
                String category = anchor.asText();

                if (!category.equals("Flowers")) {
                    result.add(new Pair(category, URL));
                }
            }


        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public CompletableFuture<Boolean> addRestaurantAndFoodInfo(String name, String url, String category) {
        return CompletableFuture.supplyAsync(() -> {
            DatabaseCon db = new DatabaseCon();

            boolean toAddFood = db.addRestaurant(name, category);
            if (toAddFood) {
                int restaurant_id = db.getRestaurantID(name);

                try {
                    List<Item> items = getFoodInfo(url);
                    if (items.size() != 0) {
                        db.addFoodInfo(items, restaurant_id);
                    }
                } catch (FailingHttpStatusCodeException e) {
                    System.out.println("Page is error.");
                }
            }
            return true;
        });
    }

    public static void main(String[] args) {
        Extract ex = new Extract();
        DatabaseCon db = new DatabaseCon();

        List<Pair> categories = ex.getCategory("https://www.foodpanda.sg/");

        for (Pair category : categories) {
            String cat = category.getName();
            String URL = category.getURL();

            db.addCategory(cat);
            List<Pair> pairs = ex.getRestaurantInfo(URL);
            List<CompletableFuture<Boolean>> async = new ArrayList<>();

            for (Pair pair : pairs) {
                String restaurantName = pair.getName();
                String url = pair.getURL();
                System.out.println(url);
                System.out.println(restaurantName);

                if (!restaurantName.startsWith("pandamart")) {
                    CompletableFuture<Boolean> addInfo = ex.addRestaurantAndFoodInfo(restaurantName, url, cat);
                    async.add(addInfo);
                }
            }

            CompletableFuture.allOf(async.toArray(new CompletableFuture[0])).join();
        }

    }
}
