package util;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Properties properties = new Properties();

    static {
        try {
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                System.err.println("config.properties not found in classpath");
            } else {
                properties.load(input);
                input.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to load database configuration: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            String url = getConfigValue("DB_URL", "db.url");
            String user = getConfigValue("DB_USER", "db.user");
            String password = getConfigValue("DB_PASSWORD", "db.password");

            // Connection logs
            System.out.println("=== Database Configuration ===");
            System.out.println("URL: " + url);
            System.out.println("User: " + user);
            System.out.println("Password: " + (password != null ? "****" : "NULL"));
            System.out.println("=============================");

            if (url == null || user == null || password == null) {
                throw new SQLException("Database configuration is incomplete");
            }
 
            // Load PostgreSQ: driver
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found");
            e.printStackTrace();
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    /** 
     * Gets config value at first form env variable
     * If env variable doesnt exist then gets it from properties file 
     */
    private static String getConfigValue(String envVarName, String propertyName) {
        String envValue = System.getenv(envVarName);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        return properties.getProperty(propertyName);
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
