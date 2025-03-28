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

public class MealIngredientsDAO {

    private final MealDAO mealDao;
    private final IngredientDAO ingredientDao;

    public MealIngredientsDAO() {
        this.mealDao = new MealDAO();
        this.ingredientDao = new IngredientDAO();
    }

   // CREATE - Add new Meal Ingredients match to datbase
    public boolean createMealIngredients(MealIngredients mealIng) {
        String sql = "INSERT INTO meal_ingredients (meal_id, ingredient_id, quantity_required) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, mealIng.getMealId());
                stmt.setInt(2, mealIng.getIngredientId());
                stmt.setInt(3, mealIng.getReqQuantity());
                
                return stmt.executeUpdate() == 1; //check if exact one row is created
    
        } catch (SQLException e) {
            System.err.println("Error adding Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

    // READ - Get Meal Ingredients match
    public MealIngredients getMealIngredientsByIds(int mealId, int ingredientId) {
        String sql = "SELECT * FROM meal_ingredients WHERE meal_id = ? AND ingredient_id = ?";
        

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mealId);
                stmt.setInt(2, ingredientId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        int quantityReq = resultSet.getInt("quantity_required");
                        // retrieve objects for meal and ingredient from DB 
                        Meal meal = this.mealDao.getMealById(mealId);
                        Ingredient ing = this.ingredientDao.getIngredientById(ingredientId);

                        return new MealIngredients(meal, ing, quantityReq);  
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Meal Ingredients match:  " + e.getMessage());
        }
        return null;
    }

     // READ - Get list of all Meal Ingredients Matches
     public List<MealIngredients> getAllMealIngredients() {
        String sql = "SELECT * FROM meal_ingredients";
        List<MealIngredients> mealIngredientsList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        int mealId = resultSet.getInt("meal_id");
                        int ingredientId = resultSet.getInt("ingredient_id");
                        int quantityReq = resultSet.getInt("quantity_required");
                        // retrieve objects for meal and ingredient from DB 
                        Meal meal = this.mealDao.getMealById(mealId);
                        Ingredient ing = this.ingredientDao.getIngredientById(ingredientId);
                        
                        MealIngredients mealIng = new MealIngredients(meal, ing, quantityReq); 
                        mealIngredientsList.add(mealIng);
                    }
                    return mealIngredientsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Meal Ingredients list:  " + e.getMessage());
        }
        return mealIngredientsList;
    }

    // UPDATE - Update Meal Ingredients match data
    public boolean updateMealIngredients(MealIngredients mealIngredient) {
        String sql = "UPDATE meal_ingredients SET quantity_required = ? WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, mealIngredient.getReqQuantity());
                stmt.setInt(2, mealIngredient.getMealId());
                stmt.setInt(3, mealIngredient.getIngredientId());
                
                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete Meal Ingredients match by IDs
    public boolean deleteMealIngredientsByIds(int mealId, int ingredientId) {
        String sql = "DELETE FROM meal_ingredients WHERE meal_id = ? AND ingredient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mealId);
                stmt.setInt(2, ingredientId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No match found for meal with ID " + mealId + " and ingredient with ID " + ingredientId);
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting Meal Ingredients match: " + e.getMessage());
        }
        return false;
    }

}


