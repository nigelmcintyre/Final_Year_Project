package nigelmcintyre.login;

/**
 * Created by Nigel on 03/03/2016.
 */
public class Foods {
    public String FoodName;
    public double fat, carbs, protein, calories;

    public double getCalories() {
        return calories;
    }

    public String getFoodName() {
        return FoodName;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public String toString(){
        return this.carbs+","+this.fat+","+this.protein+","+this.calories;
    }
}
