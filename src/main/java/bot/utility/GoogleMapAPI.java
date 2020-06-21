package bot.utility;

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
    public static String findCoordinates = "https://maps.googleapis.com/maps/api/geocode/json";
    public static String charset = "UTF-8";


    public GoogleMapAPI() {
    }

    public String findPlace(String place) {
        String host = this.findPlaceFromString;
        String key = this.key;

        String inputtype = "textquery";

        try {
            String url = String.format("key=%s&input=%s&inputtype=%s&fields=name",
                    URLEncoder.encode(key, charset),
                    URLEncoder.encode(place, charset),
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

    public PlaceInfo getPlaceCoordinates(String place) {
        String host = this.findCoordinates;
        String key = this.key;

        try {
            String url = String.format("key=%s&address=%s",
                    URLEncoder.encode(key, charset),
                    URLEncoder.encode(place, charset));

            HttpResponse<JsonNode> response = Unirest.get(host + "?" + url)
                    .asJson();

            JsonNode body = response.getBody();

            LinkedTreeMap treeMap = (LinkedTreeMap) ((LinkedTreeMap) body.getObject().getJSONArray("results")
                    .getJSONObject(0).toMap().get("geometry")).get("location");

            double longitude = (double) treeMap.get("lng");
            System.out.println("Longitude in google:" + longitude);
            double latitude = (double) treeMap.get("lat");


            return new PlaceInfo(longitude, latitude);
        } catch (UnsupportedEncodingException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }

    }
}
