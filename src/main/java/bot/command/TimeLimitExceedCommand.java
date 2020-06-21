package bot.command;

public class TimeLimitExceedCommand implements Command {

    @Override
    public String execute() {
        return "Time's up! Please start ordering.";
    }
}
