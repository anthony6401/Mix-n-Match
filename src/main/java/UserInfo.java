public class UserInfo {
    private final long chat_id;
    private final double longitude;
    private final double latitude;

    UserInfo(long chat_id, double longitude, double latitude) {
        this.chat_id = chat_id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getChatID() {
        return this.chat_id;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public String toString() {
        return "Chat_id= " + this.chat_id + ", Longitude= " + this.longitude + ", Latitude= " + this.latitude;
    }
}
