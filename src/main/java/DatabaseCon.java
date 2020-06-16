import java.sql.*;

public class DatabaseCon {
    public static String userdbURL = "jdbc:mysql://localhost:3306/usersdb?serverTimezone=MST";
    public static String restaurantURL = "jdbc:mysql://localhost:3306/restaurantdb?serverTimezone=MST";
    public static String user = "root";
    public static String password = "Password123";
    private Statement stmtUser;
    private Statement stmtRes;

    DatabaseCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conUser = DriverManager.getConnection(userdbURL, user, password);
            Connection conRes = DriverManager.getConnection(restaurantURL, user, password);
            Statement stmtUser = conUser.createStatement();
            Statement stmtRes = conRes.createStatement();
            this.stmtUser = stmtUser;
            this.stmtRes = stmtRes;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeRestaurantDatabase() {
        try {
            String query1 = "DELETE FROM food_list WHERE food_id >= 1;";
            String query2 = "DELETE FROM restaurant_list WHERE restaurant_id >= 1;";
            String query3 = "ALTER TABLE `restaurantdb`.`restaurant_list` AUTO_INCREMENT = 1;";
            String query4 = "ALTER TABLE `restaurantdb`.`food_list` AUTO_INCREMENT = 1;";
            String query5 = "DELETE FROM category_list WHERE category_id >= 1;";
            String query6 = "ALTER TABLE `restaurantdb`.`category_list` AUTO_INCREMENT = 1;";
            String query7 = "DELETE FROM restaurant_category WHERE rc_id >= 1;";
            String query8 = "ALTER TABLE `restaurantdb`.`restaurant_category` AUTO_INCREMENT = 1;";

            stmtRes.addBatch(query1);
            stmtRes.addBatch(query2);
            stmtRes.addBatch(query3);
            stmtRes.addBatch(query4);
            stmtRes.addBatch(query5);
            stmtRes.addBatch(query6);
            stmtRes.addBatch(query7);
            stmtRes.addBatch(query8);

            stmtRes.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean containsUser(String username) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Telegram_username\n" +
                    "FROM user\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            rs.next();
            return rs.getString(1).equals(username);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isOnline(String username) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Status\n" +
                    "FROM user\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            rs.next();
            return rs.getInt(1) == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateOnline(String username) {
        try {
            stmtUser.execute("UPDATE user\n" +
                    "SET status = 1\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateOffline(String username) {
        try {
            stmtUser.execute("UPDATE user\n" +
                    "SET status = 2\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getMobileNumber(String username) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Mobile_number\n" +
                    "FROM user\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return null;
        }
    }

    public String getPassword(String username) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT password\n" +
                    "FROM user\n" +
                    "WHERE Telegram_username = \"" + username + "\"");
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean addUser(String username, String password) {
        try {
            stmtUser.execute("INSERT INTO user\n" +
                    "VALUES (DEFAULT, \"" + username + "\", \"" + password + "\", 2)");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getRestaurantID(String name) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT restaurant_id\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name = \"" + name + "\"");


            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    public int getCategoryID(String category) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT category_id\n" +
                    "FROM category_list\n" +
                    "WHERE name = \"" + category + "\"");

            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    public boolean hasRestaurant(String name) {
        return getRestaurantID(name) != -1;
    }


    public boolean addRestaurant(String name, String category) {
        try {
            int categoryId = getCategoryID(category);
            String categoryQuery = "INSERT INTO restaurant_category\n" +
                    "VALUES(DEFAULT, \"" + name + "\", " + categoryId + ")";

            stmtRes.execute(categoryQuery);

            if (!hasRestaurant(name)) {
                String query = "INSERT INTO restaurant_list\n" +
                        "VALUES (DEFAULT, \"" + name + "\")";

                stmtRes.execute(query);
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean addFoodInfo(Item item, int restaurant_id) {
        try {
            String query = "INSERT INTO food_list\n" +
                    "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                    item.getPrice() + ")";

            stmtRes.execute(query);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    public boolean addCategory(String category) {
        try {
            String query = "INSERT INTO category_list\n" +
                    "VALUES (DEFAULT, \"" + category + "\")";

            stmtRes.execute(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAllCategory(int page) {
        try {
            ResultSet rs;
            StringBuilder sb = new StringBuilder();
            if (page == 1) {
                rs = stmtRes.executeQuery("SELECT name\n" +
                        "FROM category_list\n" +
                        "WHERE category_id >= 1 AND category_id <= 20");
            } else if (page == 2) {
                rs = stmtRes.executeQuery("SELECT name\n" +
                        "FROM category_list\n" +
                        "WHERE category_id > 20");
            } else {
                return "Invalid page. There are only 2 pages!";
            }

            sb.append("Showing page " + page + " from 2:\n");

            while (rs.next()) {
                sb.append(rs.getString(1) + "\n");
            }

            sb.append("To change page, please use /categorylist [page]");

            return sb.toString();
        } catch (SQLException e) {
            return "";
        }
    }

    public String orderFromSearch(String restaurant) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT name\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"%" + restaurant + "%\"");
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return "No restaurant found";
        }
    }

    public String getRestaurantList(String category) {
        try {
            StringBuilder sb = new StringBuilder();

            ResultSet rsID = stmtRes.executeQuery("SELECT *\n" +
                    "FROM category_list\n" +
                    "WHERE name LIKE \"" + category + "%\"");


            rsID.next();
            String categorySearched = rsID.getString(2);
            int category_id = rsID.getInt(1);

            ResultSet rs = stmtRes.executeQuery("SELECT restaurant_name\n" +
                    "FROM restaurant_category\n" +
                    "WHERE category_id = " + category_id);

            sb.append("Restaurant in " + categorySearched + " category:\n");
            while (rs.next()) {
                sb.append(rs.getString(1) + "\n");
            }

            return sb.toString().trim();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Invalid category. Please use /categorylist to see all " +
                    "the available category.";
        }
    }

    public String getRestaurantMenu(String restaurant) {
        try {
            StringBuilder sb = new StringBuilder();

            ResultSet rsID = stmtRes.executeQuery("SELECT *\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"" + restaurant + "%\"");


            rsID.next();
            String restaurantSearched = rsID.getString(2);
            int restaurant_id = rsID.getInt(1);

            ResultSet rs = stmtRes.executeQuery("SELECT name, price\n" +
                    "FROM food_list\n" +
                    "WHERE restaurant_id = " + restaurant_id);

            sb.append(restaurantSearched + "\'s menu:\n");
            while (rs.next()) {
                sb.append(rs.getString(1) + " -- $" + rs.getFloat(2) + "\n");
            }

            return sb.toString().trim();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Invalid restaurant. Please use /restaurantsearch to see all" +
                    "the available restaurant.";
        }
    }

    public String searchRestaurant(String keyword) {
        try {
            StringBuilder sb = new StringBuilder();

            ResultSet rs = stmtRes.executeQuery("SELECT name\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"%" + keyword + "%\"");

            sb.append("Restaurant avalaible:\n");

            while (rs.next()) {
                sb.append(rs.getString(1) + "\n");
            }
            return sb.toString().trim();
        } catch (SQLException e) {
            e.printStackTrace();
            return "No restaurant found";
        }
    }

    public Item findItem(String item, int restaurantID) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT name, price\n" +
                    "FROM food_list\n" +
                    "WHERE restaurant_id = " + restaurantID + " AND name LIKE \"%" + item + "%\"");
            rs.next();

            return new Item(rs.getString(1), rs.getDouble(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        //testing purposes
//        final long startTime = System.nanoTime();
//        DatabaseCon db = new DatabaseCon();
//        db.initializeRestaurantDatabase();
//        final long duration = System.nanoTime() - startTime;
//        System.out.println("Duration: " + duration/1000000000F);

        DatabaseCon db = new DatabaseCon();
        System.out.println(db.getMobileNumber("anthony_tony"));
        //db.addRestaurant("LiHo","Bubble Tea");
        //db.addUser(1231241231, "inipassword1231");
        //System.out.println(db.getPassword(12345678));
        //db.updateOnline(12312412);

    }
}
