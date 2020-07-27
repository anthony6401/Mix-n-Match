package web.extract;

import bot.utility.CategoryInfo;
import bot.utility.DatabaseCon;
import bot.utility.ItemForDB;
import bot.utility.RestaurantInfo;
import bot.utility.CategoryForDB;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Extract {
    private WebClient client;
    public DatabaseCon db = new DatabaseCon();

    public Extract() {
        WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);
        client.setCssEnabled(false);
        try {
            client.setUseInsecureSSL(true);
            this.client = client;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    public List<ItemForDB> getFoodInfo(String url, int restaurant_id) {
        List<ItemForDB> result = new ArrayList<>();
        try {
            HtmlPage page = client.getPage(url);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//li[contains(@class, 'dish-card')]");

            if (items.isEmpty()) {
                System.out.println("No items found");
            } else {
                for (HtmlElement element : items) {
                    HtmlParagraph descParagraph = element.getFirstByXPath("div/div/div/p");
                    HtmlElement imageElement = element.getFirstByXPath("div/div/picture");
                    HtmlSpan name = element.getFirstByXPath("div/div/div/h3/span");
                    HtmlSpan price = element.getFirstByXPath("div/section/div/span");

                    String desc = null;
                    if (descParagraph != null) {
                        desc = descParagraph.asText();
                    }
                    String imageURL = null;
                    if (imageElement != null) {
                        imageURL = ((HtmlElement) imageElement.getFirstByXPath("div"))
                                .getAttribute("data-src");
                    }
                    String sPrice = price.asText();
                    float priceNumber = Float.valueOf(sPrice.split("\\$")[1]);

                    ItemForDB item = new ItemForDB(name.asText(), priceNumber, desc, imageURL, restaurant_id);
                    result.add(item);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<RestaurantInfo> getRestaurantInfo(String url, int category_id) {
        List<RestaurantInfo> result = new ArrayList<>();

        try {
            HtmlPage page = client.getPage(url);
            System.out.println(url);
            List<HtmlElement> URLs = (List<HtmlElement>) page.getByXPath("//li/a");

            for (HtmlElement element : URLs) {
                String URL = element.getAttribute("href");
                HtmlSpan span = element.getFirstByXPath("figure/figcaption/span/span");

                if (URL.contains("/chain/") || URL.contains("/restaurant/")) {
                    String name = span.asText().replaceAll("\\(.*\\)", "").trim();
                    if (URL.startsWith("http")) {
                    } else {
                        URL = "https://www.foodpanda.sg" + URL;
                    }
                    System.out.println(URL);
                    HtmlPage pageDeliveryHours = client.getPage(URL + "#restaurant-info");
                    HtmlElement deliveryHoursElement = pageDeliveryHours
                            .getFirstByXPath("//ul[@class=\"vendor-delivery-times\"]/li");
                    String deliveryHours = null;

                    if (deliveryHoursElement != null) {
                        deliveryHours = deliveryHoursElement.asText();
                    }

                    result.add(new RestaurantInfo(name, URL, deliveryHours, category_id));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<CategoryInfo> getCategory(String url) {
        List<CategoryInfo> result = new ArrayList<>();

        try {
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

                if (!category.equals("Flowers") && !category.equals("Groceries")) {
                    result.add(new CategoryInfo(category, URL));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public CompletableFuture<Boolean> addRestaurantAndFoodInfo(RestaurantInfo restaurantInfo,
                                                               int restaurant_id,
                                                               List<ItemForDB> list, Map<RestaurantInfo, Integer> restaurantMap) {
        return CompletableFuture.supplyAsync(() -> {
            boolean toAddFood = !restaurantMap.containsKey(restaurantInfo);
            if (toAddFood) {
                restaurantMap.put(restaurantInfo, restaurant_id);
                try {
                    String url = restaurantInfo.getURL();
                    List<ItemForDB> items = getFoodInfo(url, restaurant_id);
                    if (items.size() != 0) {
                        list.addAll(items);
                    }
                } catch (FailingHttpStatusCodeException e) {
                    System.out.println("Page is error.");
                }
            }
            return true;
        });
    }

    public void extract(String foodPandaURL) {
        DatabaseCon db = new DatabaseCon();
        int count = 0;
        int restaurant_id = 1;
        int category_id = 1;
        List<CategoryInfo> categories = getCategory(foodPandaURL);
        List<CategoryForDB> categoryList = new ArrayList<>();
        List<ItemForDB> foodList = new ArrayList<>();
        Map<RestaurantInfo, Integer> restaurantMap = new HashMap<>();
        for (CategoryInfo category : categories) {

            String cat = category.getCategory();
            String URL = category.getURL();

            categoryList.add(new CategoryForDB(cat, category_id));
            List<RestaurantInfo> restaurantInfos = getRestaurantInfo(URL, category_id);
            List<CompletableFuture<Boolean>> async = new ArrayList<>();

            for (RestaurantInfo restaurantInfo : restaurantInfos) {
                CompletableFuture<Boolean> addInfo = addRestaurantAndFoodInfo(restaurantInfo,
                        restaurant_id, foodList, restaurantMap);
                restaurant_id++;
                async.add(addInfo);
                count++;

            }

            category_id++;
            try {
                CompletableFuture.allOf(async.toArray(new CompletableFuture[0])).join();
            } catch (CompletionException e) {
                e.printStackTrace();
            }

        }



        db.addRestaurant(restaurantMap);
        db.addCategory(categoryList);
        db.addFoodInfo(foodList);

    }
}
