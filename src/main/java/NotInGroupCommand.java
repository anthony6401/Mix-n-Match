public class NotInGroupCommand implements Command {

    NotInGroupCommand() {

    }

    @Override
    public String execute() {
        return "You are not in group. Please invite the bot to a group and use /help to start using the bot.";
    }
}
