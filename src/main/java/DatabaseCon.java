import java.sql.*;
import java.util.List;

public class DatabaseCon {
    public static String userdbURL = "jdbc:mysql://localhost:3306/usersdb?serverTimezone=MST";
    public static String restaurantURL = "jdbc:mysql://localhost:3306/restaurantdb?serverTimezone=MST";
    public static String user = "root";
    public static String password = "Password123";

    public void initializeRestaurantDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, password);
            Statement stmt = con.createStatement();

            String query1 = "DELETE FROM food_list WHERE food_id >= 1;";
            String query2 = "DELETE FROM restaurant_list WHERE restaurant_id >= 1;";
            String query3 = "ALTER TABLE `restaurantdb`.`restaurant_list` AUTO_INCREMENT = 1;";
            String query4 = "ALTER TABLE `restaurantdb`.`food_list` AUTO_INCREMENT = 1;";
            String query5 = "DELETE FROM category_list WHERE category_id >= 1;";
            String query6 = "ALTER TABLE `restaurantdb`.`category_list` AUTO_INCREMENT = 1;";
            String query7 = "DELETE FROM restaurant_category WHERE rc_id >= 1;";
            String query8 = "ALTER TABLE `restaurantdb`.`restaurant_category` AUTO_INCREMENT = 1;";

            stmt.addBatch(query1);
            stmt.addBatch(query2);
            stmt.addBatch(query3);
            stmt.addBatch(query4);
            stmt.addBatch(query5);
            stmt.addBatch(query6);
            stmt.addBatch(query7);
            stmt.addBatch(query8);

            stmt.executeBatch();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean containsUser(long chat_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT username\n" +
                    "FROM user\n" +
                    "WHERE username = " + chat_id);

            rs.next();
            return rs.getLong(1) == chat_id;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public boolean isOnline(long chat_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT Status\n" +
                    "FROM user\n" +
                    "WHERE username = " + chat_id);

            rs.next();
            return rs.getInt(1) == 1;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public String getPassword(long chat_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT password\n" +
                    "FROM user\n" +
                    "WHERE username = " + chat_id);

            rs.next();
            return rs.getString(1);
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

    public boolean updateOnline(long chat_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, password);
            Statement stmt = con.createStatement();

            return stmt.execute("UPDATE user\n" +
                    "SET status = 1\n" +
                    "WHERE username = " + chat_id);
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public boolean updateOffline(long chat_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, password);
            Statement stmt = con.createStatement();

            return stmt.execute("UPDATE user\n" +
                    "SET status = 2\n" +
                    "WHERE username = " + chat_id);
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    public boolean addUser(long chat_id, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(userdbURL, user, DatabaseCon.password);
            Statement stmt = con.createStatement();

            //String query = "INSERT INTO user\n" +
                    //"VALUES (DEFAULT, 123415144, 'inipasswo1242133', 2)";
            String query = "INSERT INTO user\n" +
                   "VALUES (DEFAULT, " + chat_id + ", \"" + password + "\", 2)";


            return stmt.execute(query);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRestaurantID(String name) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT restaurant_id\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name = \"" + name + "\"");


            rs.next();
            return rs.getInt(1);
        } catch (ClassNotFoundException | SQLException e) {
            return -1;
        }
    }

    public boolean hasRestaurant(String name) {
        return getRestaurantID(name) != -1;
    }

    public int getCategoryID(String category) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT category_id\n" +
                    "FROM category_list\n" +
                    "WHERE name = \"" + category + "\"");


            rs.next();
            return rs.getInt(1);
        } catch (ClassNotFoundException | SQLException e) {
            return -1;
        }
    }

    public boolean addRestaurant(String name, String category) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, DatabaseCon.password);
            Statement stmt = con.createStatement();

            int categoryId = getCategoryID(category);
            String categoryQuery = "INSERT INTO restaurant_category\n" +
                    "VALUES(DEFAULT, \"" + name + "\", " + categoryId + ")";

            stmt.execute(categoryQuery);

            if (!hasRestaurant(name)) {
                String query = "INSERT INTO restaurant_list\n" +
                        "VALUES (DEFAULT, \"" + name + "\")";

                stmt.execute(query);
            }
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean addFoodInfo(List<Item> items, int restaurant_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, DatabaseCon.password);
            Statement stmt = con.createStatement();

            for (Item item : items) {
                String query = "INSERT INTO food_list\n" +
                        "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                        item.getPrice() + ")";

                stmt.execute(query);
            }
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }

    }

    public boolean addCategory(String category) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(restaurantURL, user, DatabaseCon.password);
            Statement stmt = con.createStatement();

            String query = "INSERT INTO category_list\n" +
                    "VALUES (DEFAULT, \"" + category + "\")";

            stmt.execute(query);
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static void main(String[] args) {
        DatabaseCon db = new DatabaseCon();
        db.initializeRestaurantDatabase();
        //db.addRestaurant("LiHo","Bubble Tea");
        //db.addUser(1231241231, "inipassword1231");
        //System.out.println(db.getPassword(12345678));
        //db.updateOnline(12312412);

    }
}
