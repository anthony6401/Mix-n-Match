public interface Command {
    DatabaseCon db = new DatabaseCon();
    GoogleMapAPI googleMap = new GoogleMapAPI();
    String execute();
}
