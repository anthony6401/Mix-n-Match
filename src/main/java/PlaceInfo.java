public class PlaceInfo {
    private final double longitude;
    private final double latitude;

    PlaceInfo(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }
}
