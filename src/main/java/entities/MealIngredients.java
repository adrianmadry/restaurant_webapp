package entities;

public class MealIngredients {

    private Meal meal;
    private Ingredient ingredient;
    private int reqQuantity;

    public MealIngredients() {

    }

    public MealIngredients(Meal meal, Ingredient ingredient, int reqQuantity) {
        this.meal = meal;
        this.ingredient = ingredient;
        this.reqQuantity = reqQuantity;
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

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getIngredientId() {
        return ingredient.getIngredientId();
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(int reqQuantity) {
        this.reqQuantity = reqQuantity;
    }

    
    @Override
    public String toString() {
        return "MealIngredients [meal=" + meal + ", ingredient=" + ingredient + ", reqQuantity=" + reqQuantity + "]";
    }

    
}
