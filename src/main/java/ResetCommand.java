public class ResetCommand implements Command {
    private final ClientOrder co;

    ResetCommand(ClientOrder co) {
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        } else {
            co.setFrom(null);
            co.setTo(null);
            return "Successfully clear the order!";
        }
    }
}
