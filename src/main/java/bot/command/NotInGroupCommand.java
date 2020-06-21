package bot.command;

public class NotInGroupCommand implements Command {

    public NotInGroupCommand() {

    }

    @Override
    public String execute() {
        return "You are not in group. Please invite the bot to a group and use /help to start using the bot.";
    }
}
