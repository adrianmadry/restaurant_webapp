package entities;


public class Meal {

    protected int mealId;
    protected String name;
    protected String type;
    protected String description;
    protected Double price;
    
    public Meal() {

    }

    public Meal(String name, String type, String description, Double price) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public Meal(int mealId, String name, String type, String description, Double price) {
        this.mealId = mealId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Meal [mealId=" + mealId + ", name=" + name + ", type=" + type + ", description=" + description
                + ", price=" + price + "]";
    }










}
