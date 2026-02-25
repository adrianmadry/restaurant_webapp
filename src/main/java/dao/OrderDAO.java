package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Order;
import entities.OrderStatus;
import util.DatabaseConnection;
import util.JdbcStatementHelper;

public class OrderDAO {

    public OrderDAO() {
        
    }

    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, status, total_price) VALUES (?, ?::order_status, ?) RETURNING order_id, order_date";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, order.getUserId(), order.getOrderStatus(), order.getTotalPrice());                
                try (ResultSet returnedKeys = statement.executeQuery()) {
                    if (returnedKeys.next()) {
                        order.setOrderId(returnedKeys.getInt("order_id"));
                        order.setOrderDate(returnedKeys.getDate("order_date").toString());
                    }
                }
                System.out.println("New order created in database");
                return true;
    
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
        return false;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, orderId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) { 
                        return mapRowFromQueryToOrder(resultSet); 
                    }
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving order:  " + e.getMessage());
        }
        return null;
    }

     public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        List<Order> orderList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Order order = mapRowFromQueryToOrder(resultSet);
                        orderList.add(order);
                    }
                    return orderList;
                }
                     
        } catch (SQLException e) {
            System.err.println("Error retrieving orders list:  " + e.getMessage());
        }
        return orderList;
    }

    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?,  status = ?::order_status, total_price = ? WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                
                JdbcStatementHelper.setStatementParams(statement, order.getUserId(), order.getOrderStatus(), order.getTotalPrice(), order.getOrderId());
                return statement.executeUpdate() == 1; //check if exact one row was updated
                
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteOrderById(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                JdbcStatementHelper.setStatementParams(statement, orderId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Order with ID " + orderId + " not found");
                }
                return rowsAffected == 1; //check if exact one row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
        return false;
    }

    private Order mapRowFromQueryToOrder(ResultSet resultSet) throws SQLException {
        return new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("total_price"),
                        resultSet.getDate("order_date").toString(),
                        OrderStatus.valueOf(resultSet.getString("status").toUpperCase())
                    );        
    }

}
