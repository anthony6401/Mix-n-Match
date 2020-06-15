public class CheckOrderStatusCommand implements Command {
    private ClientOrder co;

    CheckOrderStatusCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null || (co.getFrom() == null && co.getTo() == null)) {
            return "No order from users yet.";
        } else {
            if (co.getFrom() == null) {
                return "Ordering to " + co.getTo();
            } else if (co.getTo() == null) {
                return "Ordering from " + co.getFrom();
            } else {
                return "Ordering from " + co.getFrom() + " to " + co.getTo();
            }
        }
    }
}
