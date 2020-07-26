package bot.utility;

public class DatabaseQuery {
    public static String query1 = "USE `heroku_f8f71944af9b502`;\n" +
            "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
            "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
            "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
            "/*!50503 SET NAMES utf8 */;\n" +
            "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
            "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
            "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
            "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
            "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
            "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
            "DROP TABLE IF EXISTS `category_list`;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;\n";

    public static String CREATE_CATEGORY_LIST = "CREATE TABLE `category_list` " +
            "(`category_id` int NOT NULL AUTO_INCREMENT, `name` varchar(45) NOT NULL, PRIMARY KEY (`category_id`)" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query2 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;\n";

    public static String CREATE_FOOD_HISTORY = "CREATE TABLE IF NOT EXISTS `food_history` (\n" +
            "`fh_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`user_id` int NOT NULL,\n" +
            "`order_id` int NOT NULL,\n" +
            "`item` varchar(1000) NOT NULL,\n" +
            "  PRIMARY KEY (`fh_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query3 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;\n" +
            "DROP TABLE IF EXISTS `food_list`;";

    public static String CREATE_FOOD_LIST = "CREATE TABLE `food_list` (\n" +
            "`food_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`restaurant_id` int NOT NULL,\n" +
            "`name` varchar(300) NOT NULL,\n" +
            "`price` float NOT NULL,\n" +
            "`description` varchar(1000) DEFAULT NULL,\n" +
            "`url` varchar(300) DEFAULT NULL,\n" +
            "PRIMARY KEY (`food_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query4 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "DROP TABLE IF EXISTS `restaurant_category`;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_RESTAURANT_CATEGORY = "CREATE TABLE `restaurant_category` (\n" +
            "`rc_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`restaurant_name` varchar(100) NOT NULL,\n" +
            "`category_id` varchar(45) NOT NULL,\n" +
            "PRIMARY KEY (`rc_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query5 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "DROP TABLE IF EXISTS `restaurant_list`;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_RESTAURANT_LIST = "CREATE TABLE `restaurant_list` (\n" +
            "`restaurant_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`name` varchar(100) NOT NULL,\n" +
            "`delivery_hours` varchar(300) DEFAULT NULL,\n" +
            "PRIMARY KEY (`restaurant_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query6 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;\n" +
            "/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" +
            "/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" +
            "/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" +
            "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" +
            "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" +
            "/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;\n" +
            "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
            "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
            "/*!50503 SET NAMES utf8 */;\n" +
            "/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;\n" +
            "/*!40103 SET TIME_ZONE='+00:00' */;\n" +
            "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
            "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
            "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
            "/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_HISTORY = "CREATE TABLE IF NOT EXISTS `history` (\n" +
            "`history_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`user_id` int NOT NULL,\n" +
            "`order_id` int NOT NULL,\n" +
            "`date` varchar(45) NOT NULL,\n" +
            "`delivery_cost` float NOT NULL,\n" +
            "`order_from` varchar(100) NOT NULL,\n" +
            "`order_to` varchar(100) NOT NULL,\n" +
            "PRIMARY KEY (`history_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query7 = "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_USER_BANNED = "CREATE TABLE IF NOT EXISTS `user_banned` (\n" +
            "  `banned_id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `telegram_id` int NOT NULL,\n" +
            "  `unix_unbanned` int NOT NULL,\n" +
            "  PRIMARY KEY (`banned_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query8 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "DROP TABLE IF EXISTS `status_id`;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_STATUS_ID = "CREATE TABLE `status_id` (\n" +
            "`status_id` int NOT NULL AUTO_INCREMENT,\n" +
            "`name` varchar(45) NOT NULL,\n" +
            "PRIMARY KEY (`status_id`)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query9 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n" +
            "/*!50503 SET character_set_client = utf8 */;";

    public static String CREATE_USER = "CREATE TABLE IF NOT EXISTS `user` (\n" +
            "`user_id` bigint NOT NULL AUTO_INCREMENT,\n" +
            "`Username` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Email` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Address` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Token` varchar(400) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Telegram_id` bigint DEFAULT NULL,\n" +
            "`Mobile_number` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,\n" +
            "`Longitude` double NOT NULL,\n" +
            "`Latitude` double NOT NULL,\n" +
            "`Status` tinyint NOT NULL,\n" +
            "PRIMARY KEY (`user_id`)\n" +
            ") ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";

    public static String query10 = "/*!40101 SET character_set_client = @saved_cs_client */;\n" +
            "LOCK TABLES status_id WRITE;\n" +
            "INSERT INTO status_id VALUES (1,'ONLINE'),(2,'OFFLINE');\n" +
            "UNLOCK TABLES;\n" +
            "/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;\n" +
            "/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" +
            "/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" +
            "/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" +
            "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" +
            "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" +
            "/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;";

}
