package bot.utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCon {
    public static String userdbURL = "jdbc:mysql://localhost:3306/usersdb?serverTimezone=MST";
    public static String restaurantURL = "jdbc:mysql://localhost:3306/restaurantdb?serverTimezone=MST";
    public static String user = "root";
    public static String password = "Password123";
    private Statement stmtUser;
    private Statement stmtRes;

    public DatabaseCon() {
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

    public boolean containsUser(String telegramCode) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Telegram_code\n" +
                    "FROM user\n" +
                    "WHERE Telegram_code = \"" + telegramCode + "\"");
            rs.next();
            return rs.getString(1).equals(telegramCode);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addTelegramInformation(String telegramCode, Integer telegram_id) {
        try {
            stmtUser.execute("UPDATE user\n" +
                    "SET Telegram_id = " + telegram_id + "\n" +
                    "WHERE Telegram_code = \"" + telegramCode + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isOnline(Integer telegram_id) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Status\n" +
                    "FROM user\n" +
                    "WHERE Telegram_id = " + telegram_id);

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getHistory(Integer telegram_id) {
        try {
            int userID = getUserID(telegram_id);
            ResultSet rs = stmtUser.executeQuery("SELECT date, order_from, order_to\n" +
                    "FROM history\n" +
                    "WHERE user_id = " + userID);

            StringBuilder sb = new StringBuilder();

            while (rs.next()) {
                sb.append("Ordered from " + rs.getString(2) +
                        " to " + rs.getString(3) + " on "
                        + rs.getString(1) + "\n");
            }

            return sb.toString().trim();
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getUserID(Integer telegram_id) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT user_id\n" +
                    "FROM user\n" +
                    "WHERE Telegram_id = " + telegram_id + " AND STATUS = 1");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean addHistory(String date, Integer telegram_id, String from, String to) {
        try {
            int userID = getUserID(telegram_id);
            if (userID != -1) {
                stmtUser.execute("INSERT INTO history\n" +
                        "VALUES (DEFAULT, " + userID + ", \"" + date + "\", \"" +
                        from + "\", \"" + to + "\")");
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserInfo> getListOfOnlineUserExceptUser(Integer telegram_id) {
        List<UserInfo> result = new ArrayList<>();

        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Telegram_id, Longitude, Latitude\n" +
                    "FROM user\n" +
                    "WHERE Status = 1 AND Telegram_id != " + telegram_id);

            while (rs.next()) {
                result.add(new UserInfo(rs.getLong(1),
                        rs.getDouble(2), rs.getDouble(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateOnline(String telegramCode) {
        try {
            stmtUser.execute("UPDATE user\n" +
                    "SET status = 1\n" +
                    "WHERE Telegram_code = \"" + telegramCode + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateOffline(Integer telegram_id) {
        try {
            stmtUser.execute("UPDATE user\n" +
                    "SET status = 2\n" +
                    "WHERE Telegram_id = " + telegram_id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getMobileNumber(Integer telegram_id) {
        try {
            ResultSet rs = stmtUser.executeQuery("SELECT Mobile_number\n" +
                    "FROM user\n" +
                    "WHERE Telegram_id = " + telegram_id + " AND STATUS = 1");
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return null;
        }
    }

    public int getRestaurantID(String name) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT restaurant_id\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"%" + name + "%\"");

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


    public boolean addRestaurant(String name, int restaurant_id,
                                 int category_id, String deliveryHours) {
        try {
            String categoryQuery = "INSERT INTO restaurant_category\n" +
                    "VALUES(DEFAULT, \"" + name + "\", " + category_id + ")";

            stmtRes.execute(categoryQuery);

            if (!hasRestaurant(name)) {
                String query;
                if (deliveryHours == null) {
                    query = "INSERT INTO restaurant_list\n" +
                            "VALUES (" + restaurant_id + ", \"" + name + "\", " +
                            "DEFAULT)";
                } else {
                    query = "INSERT INTO restaurant_list\n" +
                            "VALUES (" + restaurant_id + ", \"" + name + "\", \"" +
                            deliveryHours + "\")";
                }
                stmtRes.execute(query);
            }

            stmtRes.closeOnCompletion();
            return true;

        } catch (SQLException e) {
            return false;
        }

    }


    public boolean addFoodInfo(ItemForDB item, int restaurant_id) {
        try {
            String query;
            if (item.getURL() == null && item.getDesc() == null) {
                query = "INSERT INTO food_list\n" +
                        "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                        item.getPrice() + ", DEFAULT, DEFAULT)";
            } else if (item.getURL() == null) {
                query = "INSERT INTO food_list\n" +
                        "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                        item.getPrice() + ", \"" + item.getDesc() + "\", DEFAULT)";
            } else if (item.getDesc() == null) {
                query = "INSERT INTO food_list\n" +
                        "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                        item.getPrice() + ", DEFAULT, \"" + item.getURL() + "\")";
            } else {
                query = "INSERT INTO food_list\n" +
                        "VALUES (DEFAULT, " + restaurant_id + ", \"" + item.getName() + "\", " +
                        item.getPrice() + ", \"" + item.getDesc() + "\", \"" + item.getURL() + "\")";
            }

            stmtRes.execute(query);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    public boolean addCategory(String category, int category_id) {
        try {
            String query = "INSERT INTO category_list\n" +
                    "VALUES (" + category_id + ", \"" + category + "\")";

            stmtRes.execute(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAllCategory() {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT name\n" +
                    "FROM category_list\n" +
                    "WHERE category_id >= 1");
            StringBuilder sb = new StringBuilder();

            sb.append("All avalaible categories:\n");

            while (rs.next()) {
                sb.append(rs.getString(1) + "\n");
            }

            return sb.toString().trim();
        } catch (SQLException e) {
            return "";
        }
    }

    public Pair orderFromSearch(String restaurant) {
        try {
            ResultSet rs = stmtRes.executeQuery("SELECT name, delivery_hours\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"%" + restaurant + "%\"");
            rs.next();
            return new Pair(rs.getString(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return new Pair("No restaurant found", null);
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
                    "WHERE restaurant_id = " + restaurant_id + "\n" +
                    "ORDER BY name");

            sb.append(restaurantSearched + "\'s menu:\n");
            while (rs.next()) {
                sb.append(rs.getString(1) + " -- $" + rs.getFloat(2) + "\n");
            }

            return sb.toString().trim();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Invalid restaurant. Please use /searchrestaurant to see all" +
                    "the available restaurant.";
        }
    }

    public String searchRestaurant(String keyword) {
        try {
            StringBuilder sb = new StringBuilder();

            ResultSet rs = stmtRes.executeQuery("SELECT name, delivery_hours\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"%" + keyword + "%\"");

            sb.append("Restaurant avalaible:\n");

            while (rs.next()) {
                sb.append(rs.getString(1) + " -- " + rs.getString(2) + "\n");
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

    public DescAndURL getItemDescAndURL(String item, String restaurantName) {
        try {
            int restaurantID = getRestaurantID(restaurantName);
            ResultSet rs = stmtRes.executeQuery("SELECT description, url\n" +
                    "FROM food_list\n" +
                    "WHERE restaurant_id = " + restaurantID + " AND name LIKE \"%" + item + "%\"");
            rs.next();

            return new DescAndURL(rs.getString(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main (String[] args) {
        DatabaseCon db = new DatabaseCon();
        //System.out.println(db.getRestaurantID("cafe"));
        db.getItemDescAndURL("beef meat", "cafe");
    }
}
