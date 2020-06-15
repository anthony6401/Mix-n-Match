import com.google.gson.internal.LinkedTreeMap;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class GoogleMapAPI {
    public static String key = "AIzaSyDIWEKaDIthMAPmibgP4PEIUuNeCP69fi0";
    public static String findPlaceFromString = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
    public static String queryAutoComplete = "https://maps.googleapis.com/maps/api/place/queryautocomplete/json";
    public static String charset = "UTF-8";


    GoogleMapAPI() {
    }

    public String findPlace(String arg) {
        String host = this.findPlaceFromString;
        String key = this.key;

        String inputtype = "textquery";

        try {
            String url = String.format("key=%s&input=%s&inputtype=%s&fields=name",
                    URLEncoder.encode(key, charset),
                    URLEncoder.encode(arg, charset),
                    URLEncoder.encode(inputtype, charset));

            HttpResponse<JsonNode> response = Unirest.get(host + "?" + url)
                    .asJson();

            JsonNode body = response.getBody();


            String name = ((LinkedTreeMap) ((List) ((JSONObject) body.getArray().get(0)).toMap()
                    .get("candidates")).get(0)).get("name").toString();

            return name;

        } catch (UnsupportedEncodingException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "No place found. Please be more specific!";
        }
    }

}
