package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Ingredient;
import util.DatabaseConnection;

public class IngredientDAO {

// CREATE - Add new ingredient to database
    public static boolean createIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (name, unit, stock) VALUES (?, ?, ?) RETURNING ingredient_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, ingredient.getName());
                stmt.setString(2, ingredient.getUnit());
                stmt.setDouble(3, ingredient.getStock());
                
                // passing to ingredient object data returned from db  
                try (ResultSet returnedKeys = stmt.executeQuery()) {

                    if (returnedKeys.next()) {
                        ingredient.setIngredientId(returnedKeys.getInt("ingredient_id"));
                    }
                }
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding ingredient: " + e.getMessage());
        }
        return false;
    }

    // READ - Get ingredient by ID
    public static Ingredient getIngredientById(int ingredientId) {
        String sql = "SELECT * FROM ingredients WHERE ingredient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, ingredientId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        String name = resultSet.getString("name");
                        String unit = resultSet.getString("unit");
                        Double stock = resultSet.getDouble("stock");

                        return new Ingredient(ingredientId, name, unit, stock);  
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving ingredient:  " + e.getMessage());
        }
        return null;
    }

     // READ - Get all ingredients
     public static List<Ingredient> getAllIngredients() {
        String sql = "SELECT * FROM ingredients";
        List<Ingredient> ingredientList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        Integer ingredientId = resultSet.getInt("ingredient_id");
                        String name = resultSet.getString("name");
                        String unit = resultSet.getString("unit");
                        Double stock = resultSet.getDouble("stock");

                        Ingredient ingredient = new Ingredient(ingredientId, name, unit, stock);
                        ingredientList.add(ingredient);
                    }
                    return ingredientList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving ingredients list:  " + e.getMessage());
        }
        return ingredientList;
    }

    // UPDATE - Update ingredient data
    public static boolean updateIngredient(Ingredient ingredient) {
        String sql = "UPDATE ingredients SET name = ?, unit = ?, stock = ? WHERE ingredient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setString(1, ingredient.getName());
                stmt.setString(2, ingredient.getUnit());
                stmt.setDouble(3, ingredient.getStock());
                stmt.setInt(4, ingredient.getIngredientId());

                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating ingredient: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete Ingredient by ID
    public static boolean deleteIngredientById(int ingredientId) {
        String sql = "DELETE FROM ingredients WHERE ingredient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, ingredientId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Ingredient with ID " + ingredientId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting ingredient: " + e.getMessage());
        }
        return false;
    }

}
