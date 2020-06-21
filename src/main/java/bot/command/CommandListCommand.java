package bot.command;


public class CommandListCommand implements Command {

    public CommandListCommand() {}

    @Override
    public String execute() {
        return "Here is the command list:\n" +
                "help -- Guide to use the bot\n" +
                "join -- Join the group order. After joining the group, " +
                "an invite link will be given." +
                "The group number will be given in the notification\n" +
                "logout -- Logout from your current account\n" +
                "orderfrom -- Set from where you want to order\n" +
                "orderto -- Set to where you want to order\n" +
                "invitelink -- Set the invite link to the group\n" +
                "settime -- Set the time limit of the order\n" +
                "finalizeorder -- Finalize the order and notify everyone near the location. " +
                "In order to finalize the order, you need to use /orderfrom, /orderto, /invitelink," +
                " and /settime first\n" +
                "reset -- Reset all the order. Can't be used after finalizing the order\n" +
                "categorylist -- See all the category available\n" +
                "restaurantlist -- See all the restaurant under certain category\n" +
                "menu -- See the menu of a certain restaurant\n" +
                "searchrestaurant -- Search a restaurant available with the help of a keyword\n" +
                "add -- Add food(s) to the order. Can add description by adding" +
                " -- at the end of the food order followed by description\n" +
                "remove -- Remove food(s) from the order\n" +
                "removeall -- Remove every food order of the user\n" +
                "deliverycost -- Set the delivery cost of order\n" +
                "commandlist -- See the command list\n" +
                "history -- See the history of the user";
    }
}
