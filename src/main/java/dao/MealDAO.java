package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Meal;
import util.DatabaseConnection;
import util.JdbcStatementHelper;

public class MealDAO {

    public MealDAO() {

    }

    public boolean createMeal(Meal meal) {
        String sql = "INSERT INTO meals (name, type, description, price, image_path)" +  
                      "VALUES (?, ?, ?, ?, ?) RETURNING meal_id";
                      
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, meal.getName(), meal.getType(), 
                                                        meal.getDescription(), meal.getPrice(), meal.getImagePath());                
                try (ResultSet returnedKeys = statement.executeQuery()) {

                    if (returnedKeys.next()) {
                        meal.setMealId(returnedKeys.getInt("meal_id"));
                    }
                }
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding meal: " + e.getMessage());
        }
        return false;
    }

    public Meal getMealById(int mealId) {
        String sql = "SELECT * FROM meals WHERE meal_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, mealId);                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToMeal(resultSet);
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving meal:  " + e.getMessage());
        }
        return null;
    }

    public List<Meal> getAllMeals() {
        String sql = "SELECT * FROM meals";
        List<Meal> mealsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Meal meal = mapRowFromQueryToMeal(resultSet);
                        mealsList.add(meal);
                    }
                    return mealsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving meals list:  " + e.getMessage());
        }
        return mealsList;
    }

    public boolean updateMeal(Meal meal) {
        String sql = "UPDATE meals SET name = ?, type = ?, description = ?, price = ?, image_path = ? " + 
                      "WHERE meal_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, meal.getName(), meal.getType(), meal.getDescription(), 
                                                    meal.getPrice(), meal.getImagePath(), meal.getMealId());                
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating meal: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteMealById(int mealId) {
        String sql = "DELETE FROM meals WHERE meal_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, mealId);
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected == 0) {
                    throw new SQLException("Meal with ID " + mealId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
        }
        return false;
    }

    private Meal mapRowFromQueryToMeal(ResultSet resultSet) throws SQLException {
        return new Meal(
                        resultSet.getInt("meal_id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image_path")
                    );
    }

}
