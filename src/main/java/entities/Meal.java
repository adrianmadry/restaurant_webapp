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
        this.name = capitalizeEachWord(name);
        this.type = type;
        this.description = description;
        this.price = price;
    }

    public Meal(int mealId, String name, String type, String description, Double price) {
        this.mealId = mealId;
        this.name = capitalizeEachWord(name);
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
        this.name = capitalizeEachWord(name);
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

    /*
    Function returns String with capitalized first letters of each word. 
    Rest of letters are lowercase.
    */
    private static String capitalizeEachWord(String text) {
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String firstLetter = words[i].substring(0, 1).toUpperCase();
            String restOfWord = words[i].substring(1).toLowerCase();
            result.append(firstLetter).append(restOfWord);

            // Insert space between words in String
            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }










}
