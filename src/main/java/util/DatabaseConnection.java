package util;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connected successfully!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }


}
