import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ExecuteBot {
    public static void main(String[] args) {
        WebExtraction.main(new String[0]);
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MixnMatchBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
