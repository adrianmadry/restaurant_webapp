package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.User;
import entities.User.RoleType;
import util.DatabaseConnection;

public class UserDAO {

    public UserDAO() {
        
    }

    // CREATE - Add new user to datbase
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?::role_type) RETURNING user_id, registration_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getRole().name().toLowerCase()); // convert role_type data form db to String
                
                // passing to user object data returned from db  
                try (ResultSet returnedKeys = stmt.executeQuery()) {

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

    // READ - Get user by ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");
                        //Convert String to RoleType enum
                        String roleString = resultSet.getString("role");
                        RoleType roleType = User.RoleType.valueOf(roleString.toUpperCase());

                        String registrationDate = resultSet.getString("registration_date");
    
                        return new User(userId, username, password, email, roleType, registrationDate);  
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving user:  " + e.getMessage());
        }
        return null;
    }

     // READ - Get all users
     public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> usersList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        Integer userId = resultSet.getInt("user_id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");
                        //Convert String to RoleType enum
                        String roleString = resultSet.getString("role");
                        RoleType roleType = User.RoleType.valueOf(roleString.toUpperCase());

                        String registrationDate = resultSet.getString("registration_date");
    
                        User user = new User(userId, username, password, email, roleType, registrationDate);
                        usersList.add(user);
                    }
                    return usersList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving users list:  " + e.getMessage());
        }
        return usersList;
    }

    // UPDATE - Update user data
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ?::role_type WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getRole().name().toLowerCase());
                stmt.setInt(5, user.getUserId());

                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete User by ID
    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                int rowsAffected = stmt.executeUpdate();
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet resultSet = stmt.executeQuery();
                return resultSet.next();
        
        } catch (SQLException e) {
            System.err.println("Error authenticate user: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        UserDAO userDao = new UserDAO();
        System.out.println(userDao.authenticate("adr@gma6il.com", "passy"));
        System.out.println(userDao.authenticate("julko@gmail.com", "haslo"));
    }

}
