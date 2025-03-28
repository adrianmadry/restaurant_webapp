package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Order;
import entities.Order.OrderStatus;
import util.DatabaseConnection;

public class OrderDAO {

    // CREATE - Add new order to database
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, status) VALUES (?, ?) RETURNING order_id, order_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, order.getUserId());
                stmt.setString(2, order.getOrderStatus().toString());
                
                // passing to order object data returned from db  
                try (ResultSet returnedKeys = stmt.executeQuery()) {

                    if (returnedKeys.next()) {
                        order.setOrderId(returnedKeys.getInt("order_id"));
                        order.setOrderDate(returnedKeys.getDate("order_date").toString());

                    }
                }
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
        return false;
    }

    // READ - Get order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    if (resultSet.next()) { 
                        int userId = resultSet.getInt("user_id");
                        String orderDate = resultSet.getDate("order_date").toString();
                        // convert string to OrderStatus enum
                        String statusString = resultSet.getString("status");
                        OrderStatus status = OrderStatus.valueOf(statusString.toUpperCase());

                        return new Order(orderId, userId, orderDate, status); 
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving order:  " + e.getMessage());
        }
        return null;
    }

     // READ - Get all orders
     public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        List<Order> orderList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // iterate through data retrieved from DB
                    while (resultSet.next()) {
                        Integer orderId = resultSet.getInt("order_id");
                        Integer userId = resultSet.getInt("user_id");
                        String orderDate = resultSet.getDate("order_date").toString();
                        // convert string to OrderStatus enum
                        String statusString = resultSet.getString("status");
                        OrderStatus status = OrderStatus.valueOf(statusString.toUpperCase());

                        Order order = new Order(orderId, userId, orderDate, status);
                        orderList.add(order);
                    }
                    return orderList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving orders list:  " + e.getMessage());
        }
        return orderList;
    }

    // UPDATE - Update order data
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?,  status = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                // complete sql statement
                stmt.setInt(1, order.getUserId());
                stmt.setString(2, order.getOrderStatus().toString());
                stmt.setInt(3, order.getOrderId());

                return stmt.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
        }
        return false;
    }

    // DELETE - Delete order by ID
    public boolean deleteOrderById(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Order with ID " + orderId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
        return false;
    }

}
