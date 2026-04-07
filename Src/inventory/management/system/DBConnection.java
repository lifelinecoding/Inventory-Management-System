package inventory.management.system;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnection {

    private static String URL;
    private static String USER;
    private static String PASS;

    public static Connection connection;

    static {
        try {
            Properties p = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            p.load(fis);

            URL = p.getProperty("DB_URL");
            USER = p.getProperty("DB_USERNAME");
            PASS = p.getProperty("DB_PASSWORD");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {

        try {
            if (connection != null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}