package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.User;
import entities.RoleType;
import util.DatabaseConnection;
import util.JdbcStatementHelper;

public class UserDAO {

    public UserDAO() {
        
    }

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?::role_type) RETURNING user_id, registration_date";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());               
                try (ResultSet returnedKeys = statement.executeQuery()) {
                    if (returnedKeys.next()) {
                        user.setUserId(returnedKeys.getInt("user_id"));
                        user.setRegistrationDate(returnedKeys.getDate("registration_date").toString());
                    }
                }
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
        return false;
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToUser(resultSet); 
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving user:  " + e.getMessage());
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToUser(resultSet); 
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving user:  " + e.getMessage());
        }
        return null;
    }

     public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> usersList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = mapRowFromQueryToUser(resultSet);
                        usersList.add(user);
                    }
                    return usersList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving users list:  " + e.getMessage());
        }
        return usersList;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ?::role_type WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getUserId());               
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, userId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("User with ID " + userId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    public boolean authenticate(String email, String password) {
        String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, email, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
        
        } catch (SQLException e) {
            System.err.println("Error authenticate user: " + e.getMessage());
        }
        return false;
    }

    private User mapRowFromQueryToUser(ResultSet resultSet) throws SQLException {
        return new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    RoleType.valueOf(resultSet.getString("role").toUpperCase()),
                    resultSet.getString("registration_date")
        );
    }

}
