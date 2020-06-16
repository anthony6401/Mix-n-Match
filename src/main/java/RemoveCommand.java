public class RemoveCommand implements Command {
    private final String orderList;
    private final String username;
    private final ClientOrder co;

    RemoveCommand(String orderList, String username, ClientOrder co) {
        this.orderList = orderList;
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        boolean notFound = false;
        StringBuilder sb = new StringBuilder();
        sb.append("This items can't be found:\n");
        String[] arr = orderList.split("\n");
        for (String item : arr) {
            try {
                if (!co.deleteOrder(username, item)) {
                    notFound = true;
                    sb.append(item + "\n");
                }
            } catch (NoSuchUserExistException e) {
                return "The user can't be found. Please reset using /logout " +
                        "and login using the link provided in the website!";
            }
        }

        if (notFound) {
            return sb.toString().trim();
        } else {
            return "Successfully removing the orders!";
        }
    }
}
