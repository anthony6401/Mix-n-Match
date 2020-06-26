package bot.command;

import bot.utility.DescAndURL;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class FoodInfoCommand implements Command {
    public final String arg;
    public final long chat_id;
    private boolean sendPic;
    public final SendPhoto photoMessage;

    public FoodInfoCommand(String arg, long chat_id) {
        this.arg = arg;
        this.chat_id = chat_id;
        this.photoMessage = new SendPhoto();
    }

    public SendPhoto getPhotoMessage() {
        return this.photoMessage;
    }

    public boolean isSendPic() {
        return this.sendPic;
    }

    @Override
    public String execute() {
        if (arg == null) {
            return "You did not specify any restaurant or food! Please use this command in the format of " +
                    "/foodinfo [restaurant] | [food].";
        }
        String[] arr = arg.split("\\|");

        if (arr.length == 1) {
            return "You only specify restaurant or food! Please use this command in the format of " +
                    "/foodinfo [restaurant] | [food].";
        }

        String restaurant = arr[0].trim();
        String food = arr[1].trim();

        DescAndURL descAndURL = db.getItemDescAndURL(food, restaurant);

        if (descAndURL == null) {
            return "No restaurant or food was found!";
        }

        if (descAndURL.getDesc() == null && descAndURL.getURL() == null) {
            return "There is no description or photo for this food!";
        } else if (descAndURL.getDesc() == null) {
            sendPic = true;
            photoMessage.setChatId(chat_id);
            photoMessage.setPhoto(descAndURL.getURL());
            return "No description found.";
        } else if (descAndURL.getURL() == null) {
            return descAndURL.getDesc();
        } else {
            sendPic = true;
            photoMessage.setChatId(chat_id);
            photoMessage.setPhoto(descAndURL.getURL());
            return descAndURL.getDesc();
        }
    }
}
