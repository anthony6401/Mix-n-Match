package bot.command;

public class GroupIsUsedCommand implements Command{

    @Override
    public String execute() {
        return "You have used this group! Please make another group.";
    }
}
