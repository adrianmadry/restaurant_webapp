package entities;

public class OrderMeals {

    protected Order order;
    protected Meal meal;
    protected int quantity;

    public OrderMeals() {

    }

    public OrderMeals(Order order, Meal meal, int quantity) {
        this.order = order;
        this.meal = meal;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public int getOrderId() {
        return order.getOrderId();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Meal getMeal() {
        return meal;
    }

    public int getMealId() {
        return meal.getMealId();
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderMeals [order=" + order.getOrderId() + ", meal=" + meal.getName() + ", quantity=" + quantity + "]";
    }
 

}
