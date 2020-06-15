public class AddCommand implements Command {
    private final String orderList;
    private final String username;
    private final ClientOrder co;

    AddCommand(String orderList, String username, ClientOrder co) {
        this.orderList = orderList;
        this.username = username;
        this.co = co;
    }

    @Override
    public String execute() {
        if (co == null) {
            return "You have not set any order. Please use /orderfrom or /orderto first!";
        }

        if (co.getFrom() == null) {
            return "Please specify where do you want to order from before adding orders!";
        }
        boolean notFound = false;
        StringBuilder sb = new StringBuilder();
        sb.append("This items can't be found:\n");
        String[] arr = orderList.split("\n");
        int restaurantID = db.getRestaurantID(co.getFrom());
        for (String item : arr) {
            Item itemSearched = db.findItem(item, restaurantID);
            if (itemSearched == null) {
                notFound = true;
                sb.append(item);
            } else {
                try {
                    co.addOrder(username, itemSearched);
                } catch (NoSuchUserExistException e) {
                    return "The user can't be found. Please reset using /logout " +
                            "and login using the link provided in the website!";
                }
            }
        }

        if (notFound) {
            return sb.toString().trim();
        } else {
            return "Successfully adding all the orders!";
        }
    }
}
