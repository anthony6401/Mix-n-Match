package bot.command;

import bot.utility.DatabaseCon;
import bot.utility.GoogleMapAPI;

public interface Command {
    DatabaseCon db = new DatabaseCon();
    GoogleMapAPI googleMap = new GoogleMapAPI();
    String execute();
}
