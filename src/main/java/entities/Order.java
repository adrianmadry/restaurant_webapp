package entities;

public class Order {

    private int orderId;
    private int userId;
    private Double totalPrice;
    private String orderDate;
    private OrderStatus orderStatus;

    public Order() {
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(int userId) {
        this.userId = userId;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(int userId, Double totalPrice) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PENDING;
    }

    public Order(int orderId, int userId, Double totalPrice, String orderDate, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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
        return "Order [orderId=" + orderId + ", userId=" + userId + ", totalPrice=" + totalPrice + ", orderDate=" + orderDate + ", orderStatus="
                + orderStatus + "]";
    }


}
