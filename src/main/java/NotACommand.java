public class NotACommand implements Command {

    @Override
    public String execute() {
        return "You input a wrong command. Please check the command list using /commandlist!";
    }
}
