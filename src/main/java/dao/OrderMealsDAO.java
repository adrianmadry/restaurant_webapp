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
import util.JdbcStatementHelper;

public class OrderMealsDAO {

    private final OrderDAO orderDao;
    private final MealDAO mealDao;

    public OrderMealsDAO() {
        this.orderDao = new OrderDAO();
        this.mealDao = new MealDAO();
    }

    public boolean createOrderMeals(OrderMeals orderMeals) {
        String sql = "INSERT INTO order_meals (order_id, meal_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

                JdbcStatementHelper.setStatementParams(statement, orderMeals.getOrderId(), orderMeals.getMealId(), orderMeals.getQuantity());                
                return statement.executeUpdate() == 1; //check if exact one row is created
    
        } catch (SQLException e) {
            System.err.println("Error adding Order Meals match: " + e.getMessage());
        }
        return false;
    }

    public OrderMeals getOrderMealsByIds(int orderId, int mealId) {
        String sql = "SELECT * FROM order_meals WHERE order_id = ? AND meal_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, orderId, mealId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToOrderMeals(resultSet);
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Order Meals match:  " + e.getMessage());
        }
        return null;
    }

    public List<OrderMeals> getAllOrderMeals() {
        String sql = "SELECT * FROM order_meals";
        List<OrderMeals> OrderMealsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        OrderMeals orderMeals = mapRowFromQueryToOrderMeals(resultSet);
                        OrderMealsList.add(orderMeals);
                    }
                    return OrderMealsList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving Order Meals list:  " + e.getMessage());
        }
        return OrderMealsList;
    }

    public boolean updateOrderMeals(OrderMeals orderMeals) {
        String sql = "UPDATE order_meals SET quantity = ? WHERE meal_id = ? AND order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, orderMeals.getQuantity(), orderMeals.getMealId(), orderMeals.getOrderId());
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating Order Meals match: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteOrderMealsByIds(int orderId, int mealId) {
        String sql = "DELETE FROM order_meals WHERE order_id = ? AND meal_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, orderId, mealId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No match found for order with ID " + orderId + " and meal with ID " + mealId);
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting Order Meals match: " + e.getMessage());
        }
        return false;
    }

    private OrderMeals mapRowFromQueryToOrderMeals(ResultSet resultSet) throws SQLException {
        Order order = this.orderDao.getOrderById(resultSet.getInt("order_id"));
        Meal meal = this.mealDao.getMealById(resultSet.getInt("meal_id"));

        return new OrderMeals(
                            order, 
                            meal, 
                            resultSet.getInt("quantity")
                        );
    }
}
