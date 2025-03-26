package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.Order;
import entities.Meal;
import entities.OrderMeals;
import util.DatabaseConnection;

public class OrderMealsDAO {

    // CREATE - Add new Order Meals match to datbase
    public static boolean createOrderMeals(OrderMeals orderMeals) {
        String sql = "INSERT INTO order_meals (order_id, meal_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, orderMeals.getOrderId());
                stmt.setInt(2, orderMeals.getMealId());
                stmt.setInt(3, orderMeals.getQuantity());
                
                return stmt.executeUpdate() == 1; //check if exact one row is created
    
        } catch (SQLException e) {
            System.err.println("Error adding Order Meals match: " + e.getMessage());
        }
        return false;
    }

    // READ - Get Order Meals match
    public static OrderMeals getOrderMealsByIds(int orderId, int mealId) {
        String sql = "SELECT * FROM order_meals WHERE order_id = ? AND meal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, mealId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        int quantity = resultSet.getInt("quantity");
                        // retrieve objects for meal and order from DB 
                        Order order = OrderDAO.getOrderById(orderId);
                        Meal meal = MealDAO.getMealById(mealId);
                        
                        return new OrderMeals(order, meal, quantity);  
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Order Meals match:  " + e.getMessage());
        }
        return null;
    }

     // READ - Get list of all Order Meals Matches
     public static List<OrderMeals> getAllOrderMeals() {
        String sql = "SELECT * FROM order_meals";
        List<OrderMeals> OrderMealsList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("order_id");
                        int mealId = resultSet.getInt("meal_id");
                        int quantity = resultSet.getInt("quantity");
                        // retrieve objects for order and from DB 
                        Order order = OrderDAO.getOrderById(orderId);
                        Meal meal = MealDAO.getMealById(mealId);

                        OrderMeals orderMeals = new OrderMeals(order, meal, quantity); 
                        OrderMealsList.add(orderMeals);
                    }
                    return OrderMealsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Order Meals list:  " + e.getMessage());
        }
        return OrderMealsList;
    }

    // UPDATE - Update Order Meals match data
    public static boolean updateOrderMeals(OrderMeals orderMeals) {
        String sql = "UPDATE order_meals SET quantity = ? WHERE meal_id = ? AND order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, orderMeals.getQuantity());
                stmt.setInt(2, orderMeals.getMealId());
                stmt.setInt(3, orderMeals.getOrderId());
                
                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating Order Meals match: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete Order Meals match by IDs
    public static boolean deleteOrderMealsByIds(int orderId, int mealId) {
        String sql = "DELETE FROM order_meals WHERE order_id = ? AND meal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, mealId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No match found for order with ID " + orderId + " and meal with ID " + mealId);
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting Order Meals match: " + e.getMessage());
        }
        return false;
    }

}
