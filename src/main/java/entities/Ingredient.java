package entities;

public class Ingredient {

    private int ingredientId;
    private String name;
    private String unit;
    private Double stock;
    
    public Ingredient() {

    }

    public Ingredient(String name, String unit, Double stock) {
        this.name = name;
        this.unit = unit;
        this.stock = stock;
    }

    public Ingredient(int ingredientId, String name, String unit, Double stock) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.stock = stock;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Ingredient [ingredientId=" + ingredientId + ", name=" + name + ", unit=" + unit + ", stock=" + stock
                + "]";
    }
    

    
}
