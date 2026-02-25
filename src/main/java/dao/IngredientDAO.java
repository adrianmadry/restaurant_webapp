package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Ingredient;
import util.DatabaseConnection;
import util.JdbcStatementHelper;

public class IngredientDAO {

    public IngredientDAO() {
        
    }

    public boolean createIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (name, unit, stock) VALUES (?, ?, ?) RETURNING ingredient_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, ingredient.getName(), ingredient.getUnit(), ingredient.getStock());
                try (ResultSet returnedKeys = statement.executeQuery()) {

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

    public Ingredient getIngredientById(int ingredientId) {
        String sql = "SELECT * FROM ingredients WHERE ingredient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, ingredientId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToIngredient(resultSet);
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving ingredient:  " + e.getMessage());
        }
        return null;
    }

    public List<Ingredient> getAllIngredients() {
        String sql = "SELECT * FROM ingredients";
        List<Ingredient> ingredientList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Ingredient ingredient = mapRowFromQueryToIngredient(resultSet);
                        ingredientList.add(ingredient);
                    }
                    return ingredientList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving ingredients list:  " + e.getMessage());
        }
        return ingredientList;
    }

    public boolean updateIngredient(Ingredient ingredient) {
        String sql = "UPDATE ingredients SET name = ?, unit = ?, stock = ? WHERE ingredient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, ingredient.getName(), ingredient.getUnit(), ingredient.getStock(), ingredient.getIngredientId());
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating ingredient: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteIngredientById(int ingredientId) {
        String sql = "DELETE FROM ingredients WHERE ingredient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, ingredientId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Ingredient with ID " + ingredientId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting ingredient: " + e.getMessage());
        }
        return false;
    }

    private Ingredient mapRowFromQueryToIngredient(ResultSet resultSet) throws SQLException {
        return new Ingredient(
                            resultSet.getInt("ingredient_id"),
                            resultSet.getString("name"),
                            resultSet.getString("unit"),
                            resultSet.getDouble("stock")
                        );
    }

}
