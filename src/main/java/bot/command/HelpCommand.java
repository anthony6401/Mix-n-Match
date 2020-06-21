package bot.command;

public class HelpCommand implements Command {

    public HelpCommand() {

    }

    @Override
    public String execute() {
        return "Welcome to Mix n Match Bot!\n" +
                "This bot helps to collate people who want to order from the same restaurant to a certain location.\n\n" +
                "Here is the step by step guide on how to use the bot:\n" +
                "1. Create an account from the website.\n" +
                "2. Press the telegram link given after creating an account.\n" +
                "3. You will be moved to telegram web/app and you can use the bot now!\n" +
                "4. Create a group and add the bot to the group.\n" +
                "5. Specify all the necessary requirements i.e. /orderfrom, /orderto, /invitelink, and /settime.\n" +
                "6. Finalize the order.\n\n" +
                "To help find whether a certain restaurant is supported by the bot, you can use /categorylist," +
                " /restaurantlist, and /searchrestaurant. To see the menu of a certain restaurant, you can use /menu.";
    }
}
