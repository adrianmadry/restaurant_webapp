package entities;

public class Order {

    public enum OrderStatus {
        PENDING, PREPARING, DELIVERED, CANCELLED
    }

    protected int orderId;
    protected int userId;
    protected String orderDate;
    protected OrderStatus orderStatus;

    public Order() {
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(int userId) {
        this.userId = userId;
        this.orderStatus = OrderStatus.PENDING;
    }


    public Order(int orderId, int userId, String orderDate, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", userId=" + userId + ", orderDate=" + orderDate + ", orderStatus="
                + orderStatus + "]";
    }


}
