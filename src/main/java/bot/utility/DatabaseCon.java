package bot.utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DatabaseCon {
    public static String url = "jdbc:mysql://localhost:3306/test_database?serverTimezone=MST";
    public static String user = "root";
    public static String password = "Password123";

    public DatabaseCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            Scanner sc1 = new Scanner(DatabaseQuery.query1);
            while (sc1.hasNext()) {
                String string = sc1.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_CATEGORY_LIST);

            Scanner sc2 = new Scanner(DatabaseQuery.query2);
            while (sc2.hasNext()) {
                String string = sc2.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_FOOD_HISTORY);

            Scanner sc3 = new Scanner(DatabaseQuery.query3);
            while (sc3.hasNext()) {
                String string = sc3.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_FOOD_LIST);

            Scanner sc4 = new Scanner(DatabaseQuery.query4);
            while (sc4.hasNext()) {
                String string = sc4.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_RESTAURANT_CATEGORY);

            Scanner sc5 = new Scanner(DatabaseQuery.query5);
            while (sc5.hasNext()) {
                String string = sc5.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_RESTAURANT_LIST);

            Scanner sc6 = new Scanner(DatabaseQuery.query6);
            while (sc6.hasNext()) {
                String string = sc6.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_HISTORY);

            Scanner sc7 = new Scanner(DatabaseQuery.query7);
            while (sc7.hasNext()) {
                String string = sc7.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_USER_BANNED);

            Scanner sc8 = new Scanner(DatabaseQuery.query8);
            while (sc8.hasNext()) {
                String string = sc8.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_STATUS_ID);


            Scanner sc9 = new Scanner(DatabaseQuery.query9);
            while (sc9.hasNext()) {
                String string = sc9.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

            stmt.execute(DatabaseQuery.CREATE_USER);

            Scanner sc10 = new Scanner(DatabaseQuery.query10);
            while (sc10.hasNext()) {
                String string = sc10.nextLine();
                System.out.println(string);
                stmt.execute(string);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean containsOrderId(long order_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM history WHERE order_id = " + order_id);
            rs.next();

            int orderID = rs.getInt(3);

            return orderID == order_id;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public int getBannedTime(long telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT unix_unbanned FROM user_banned WHERE telegram_id = " + telegram_id);

            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            return -1;
        }

    }

    public int getBannedID(long telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT telegram_id FROM user_banned WHERE telegram_id = " + telegram_id);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean addBanned(long telegram_id, int timeForUnbanned) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            int bannedID = getBannedID(telegram_id);
            System.out.println(bannedID);

            if (bannedID != -1) {
                stmt.execute("UPDATE user_banned SET unix_unbanned = " + timeForUnbanned + " WHERE telegram_id = " + telegram_id);
            } else {
                stmt.execute("INSERT INTO user_banned " +
                        "VALUES (DEFAULT, " + telegram_id + ", " + timeForUnbanned + ")");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isBanned(long telegram_id, int timeNow) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM user_banned WHERE telegram_id = " + telegram_id);

            rs.next();

            Long unbanned_time = rs.getLong(3);

            return unbanned_time - timeNow > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean containsUser(String telegramCode) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT Token\n" +
                    "FROM user\n" +
                    "WHERE Token = \"" + telegramCode + "\"");
            rs.next();
            return rs.getString(1).equals(telegramCode);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addTelegramInformation(String telegramCode, Integer telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            stmt.execute("UPDATE user\n" +
                    "SET Telegram_id = " + telegram_id + "\n" +
                    "WHERE Token = \"" + telegramCode + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isOnline(Integer telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Status\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            int userID = getUserID(telegram_id);

            ResultSet rs = stmt.executeQuery("SELECT date, order_from, order_to\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT user_id\n" +
                    "FROM user\n" +
                    "WHERE Telegram_id = " + telegram_id + " AND STATUS = 1");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean addHistory(String date, double deliveryCost, Integer telegram_id, String from, String to, long order_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            int userID = getUserID(telegram_id);
            if (userID != -1) {
                stmt.execute("INSERT INTO history\n" +
                        "VALUES (DEFAULT, " + userID + ", " + order_id + ", \"" + date + "\", " + deliveryCost + ", \"" +
                        from + "\", \"" + to + "\")");
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFoodHistory(long order_id, int telegram_id, UserOrder uo) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            int userID = getUserID(telegram_id);

            StringBuffer sb = new StringBuffer();

            sb.append("INSERT INTO food_history VALUES ");

            if (userID != -1) {
                for (Item item : uo.getOrders()) {
                    String query = "(DEFAULT, " + userID + ", " + order_id + ", \"" + item.toString() + "\"), ";
                    sb.append(query);
                }

                String query = sb.toString();
                query = query.substring(0, query.length() - 2);
                stmt.execute(query);
                return true;
            }

            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<UserInfo> getListOfOnlineUserExceptUser(Integer telegram_id) {
        List<UserInfo> result = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Telegram_id, Longitude, Latitude\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            stmt.execute("UPDATE user\n" +
                    "SET status = 1\n" +
                    "WHERE Token = \"" + telegramCode + "\"");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateOffline(Integer telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            stmt.execute("UPDATE user\n" +
                    "SET status = 2\n" +
                    "WHERE Telegram_id = " + telegram_id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public String getMobileNumber(Integer telegram_id) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Mobile_number\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT restaurant_id\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT category_id\n" +
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


    public boolean addRestaurant(Map<RestaurantInfo, Integer> restaurantMap) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmtUser = con.createStatement();

            StringBuffer sb = new StringBuffer();

            sb.append("INSERT INTO restaurant_list VALUES ");

            for (Map.Entry<RestaurantInfo, Integer> entry : restaurantMap.entrySet()) {

                int restaurant_id = entry.getValue();
                RestaurantInfo restaurantInfo = entry.getKey();

                String name = restaurantInfo.getRestaurantName();
                String deliveryHours = restaurantInfo.getDeliveryHours();
                int category_id = restaurantInfo.getCategory_id();

                String categoryQuery = "INSERT INTO restaurant_category\n" +
                        "VALUES(DEFAULT, \"" + name + "\", " + category_id + ")";

                stmtUser.execute(categoryQuery);

                if (deliveryHours == null) {
                    sb.append("(" + restaurant_id + ", \"" + name + "\", " +
                            "DEFAULT), ");
                } else {
                    sb.append("(" + restaurant_id + ", \"" + name + "\", \"" +
                            deliveryHours + "\"), ");
                }
            }

            String query = sb.toString();
            query = query.substring(0, query.length() - 2);

            stmtUser.execute(query);

            return true;

        } catch (SQLException e) {
            return false;
        }

    }


    public boolean addFoodInfo(List<ItemForDB> items) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            StringBuffer sb = new StringBuffer();

            sb.append("INSERT INTO food_list VALUES ");

            for (ItemForDB item : items) {
                String query;
                String name = item.getName().replaceAll("\"", "\\\\\"");
                String desc = null;

                if (item.getDesc() != null) {
                    desc = item.getDesc().replaceAll("\"", "\\\\\"");
                }
                if (item.getURL() == null && desc == null) {
                    query = "(DEFAULT, " + item.getRestaurant_id() + ", \"" + name + "\", " +
                            item.getPrice() + ", DEFAULT, DEFAULT), ";
                } else if (item.getURL() == null) {
                    query = "(DEFAULT, " + item.getRestaurant_id() + ", \"" + name + "\", " +
                            item.getPrice() + ", \"" + desc + "\", DEFAULT), ";
                } else if (desc == null) {
                    query = "(DEFAULT, " + item.getRestaurant_id() + ", \"" + name + "\", " +
                            item.getPrice() + ", DEFAULT, \"" + item.getURL() + "\"), ";
                } else {
                    query = "(DEFAULT, " + item.getRestaurant_id() + ", \"" + name + "\", " +
                            item.getPrice() + ", \"" + desc + "\", \"" + item.getURL() + "\"), ";
                }

                System.out.println(query);
                sb.append(query);
            }

            String queryString = sb.toString();
            String query = queryString.substring(0, queryString.length() - 2);
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean addCategory(List<CategoryForDB> list) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmtUser = con.createStatement();

            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO category_list VALUES ");


            for (CategoryForDB category : list) {
                int category_id = category.getCategory_id();
                String cat = category.getCategory();

                String query = "(" + category_id + ", \"" + cat + "\"), ";
                sb.append(query);

            }

            String query = sb.toString();
            query = query.substring(0, query.length() - 2);

            stmtUser.execute(query);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAllCategory() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT name, delivery_hours\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            StringBuilder sb = new StringBuilder();

            ResultSet rsID = stmt.executeQuery("SELECT *\n" +
                    "FROM category_list\n" +
                    "WHERE name LIKE \"" + category + "%\"");


            rsID.next();
            String categorySearched = rsID.getString(2);
            int category_id = rsID.getInt(1);

            ResultSet rs = stmt.executeQuery("SELECT rc.restaurant_name, delivery_hours\n" +
                    "FROM restaurant_category rc\n" +
                    "JOIN restaurant_list rl\n" +
                    "ON rc.restaurant_name = rl.name\n" +
                    "WHERE category_id = " + category_id);

            sb.append("Restaurant in " + categorySearched + " category:\n");
            while (rs.next()) {
                sb.append(rs.getString(1) + " -- "
                        + rs.getString(2) + "\n");
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

            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            StringBuilder sb = new StringBuilder();

            ResultSet rsID = stmt.executeQuery("SELECT *\n" +
                    "FROM restaurant_list\n" +
                    "WHERE name LIKE \"" + restaurant + "%\"");


            rsID.next();
            String restaurantSearched = rsID.getString(2);
            int restaurant_id = rsID.getInt(1);

            ResultSet rs = stmt.executeQuery("SELECT name, price\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            StringBuilder sb = new StringBuilder();

            ResultSet rs = stmt.executeQuery("SELECT name, delivery_hours\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, price\n" +
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
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            int restaurantID = getRestaurantID(restaurantName);
            ResultSet rs = stmt.executeQuery("SELECT description, url\n" +
                    "FROM food_list\n" +
                    "WHERE restaurant_id = " + restaurantID + " AND name LIKE \"%" + item + "%\"");
            rs.next();

            return new DescAndURL(rs.getString(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        DatabaseCon db = new DatabaseCon();

        db.addBanned(2, 212412313);
//        System.out.println(db.containsOrderId(-385820343));
    }

}
