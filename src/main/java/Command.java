public interface Command {
    public static DatabaseCon db = new DatabaseCon();
    public String execute();
}
