public class CheckOrderStatusCommand implements Command {
    private ClientOrder co;

    CheckOrderStatusCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        return co.toString();
    }
}
