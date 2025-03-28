package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Meal;
import util.DatabaseConnection;

public class MealDAO {

    public MealDAO() {

    }

    // CREATE - Add new meal to datbase
    public boolean createMeal(Meal meal) {
        String sql = "INSERT INTO meals (name, type, description, price)" +  
                      "VALUES (?, ?, ?, ?) RETURNING meal_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, meal.getName());
                stmt.setString(2, meal.getType());
                stmt.setString(3, meal.getDescription());
                stmt.setDouble(4, meal.getPrice()); 
                
                // passing to user object data returned from db  
                try (ResultSet returnedKeys = stmt.executeQuery()) {

                    if (returnedKeys.next()) {
                        meal.setMealId(returnedKeys.getInt("meal_id"));
                    }
                }
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
        return false;
    }

    // READ - Get meal by ID
    public Meal getMealById(int mealId) {
        String sql = "SELECT * FROM meals WHERE meal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mealId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String description = resultSet.getString("description");
                        Double price = resultSet.getDouble("price");

                        return new Meal(mealId, name, type, description, price);  
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving meal:  " + e.getMessage());
        }
        return null;
    }

    // READ - Get all meals
    public List<Meal> getAllMeals() {
        String sql = "SELECT * FROM meals";
        List<Meal> mealsList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        Integer mealId = resultSet.getInt("meal_id");
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String description = resultSet.getString("description");
                        Double price = resultSet.getDouble("price");

                        Meal meal = new Meal(mealId, name, type, description, price);
                        mealsList.add(meal);
                    }
                    return mealsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving meals list:  " + e.getMessage());
        }
        return mealsList;
    }

    // UPDATE - Update meal data
    public boolean updateMeal(Meal meal) {
        String sql = "UPDATE meals SET name = ?, type = ?, description = ?, price = ? " + 
                      "WHERE meal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, meal.getName());
                stmt.setString(2, meal.getType());
                stmt.setString(3, meal.getDescription());
                stmt.setDouble(4, meal.getPrice());
                stmt.setInt(5, meal.getMealId());
                
                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating meal: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete Meal by ID
    public boolean deleteMealById(int mealId) {
        String sql = "DELETE FROM meals WHERE meal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mealId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Meal with ID " + mealId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
        }
        return false;
    }

}
