package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Ingredient;
import entities.Meal;
import entities.MealIngredients;
import util.DatabaseConnection;
import util.JdbcStatementHelper;

public class MealIngredientsDAO {

    private final MealDAO mealDao;
    private final IngredientDAO ingredientDao;

    public MealIngredientsDAO() {
        this.mealDao = new MealDAO();
        this.ingredientDao = new IngredientDAO();
    }

    public boolean createMealIngredients(MealIngredients mealIng) {
        String sql = "INSERT INTO meal_ingredients (meal_id, ingredient_id, quantity_required) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, mealIng.getMealId(),  mealIng.getIngredientId(), mealIng.getReqQuantity());                
                return statement.executeUpdate() == 1; //check if exact one row is created
    
        } catch (SQLException e) {
            System.err.println("Error adding Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

    public MealIngredients getMealIngredientsByIds(int mealId, int ingredientId) {
        String sql = "SELECT * FROM meal_ingredients WHERE meal_id = ? AND ingredient_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, mealId, ingredientId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToMealIngredient(resultSet); 
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Meal Ingredients match:  " + e.getMessage());
        }
        return null;
    }

     public List<MealIngredients> getAllMealIngredients() {
        String sql = "SELECT * FROM meal_ingredients";
        List<MealIngredients> mealIngredientsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {                        
                        MealIngredients mealIng = mapRowFromQueryToMealIngredient(resultSet);
                        mealIngredientsList.add(mealIng);
                    }
                    return mealIngredientsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Meal Ingredients list:  " + e.getMessage());
        }
        return mealIngredientsList;
    }

    public boolean updateMealIngredients(MealIngredients mealIngredient) {
        String sql = "UPDATE meal_ingredients SET quantity_required = ? WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, mealIngredient.getReqQuantity(), mealIngredient.getMealId(), mealIngredient.getIngredientId());                
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteMealIngredientsByIds(int mealId, int ingredientId) {
        String sql = "DELETE FROM meal_ingredients WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, mealId, ingredientId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No match found for meal with ID " + mealId + " and ingredient with ID " + ingredientId);
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

    private MealIngredients mapRowFromQueryToMealIngredient(ResultSet resultSet) throws SQLException {
        Meal meal = this.mealDao.getMealById(resultSet.getInt("meal_id"));
        Ingredient ingredient = this.ingredientDao.getIngredientById(resultSet.getInt("ingredient_id"));

        return new MealIngredients(
                                    meal, 
                                    ingredient, 
                                    resultSet.getInt("quantity_required")
                                );

    }

}


