package nigelmcintyre.login;

import java.sql.Date;

/**
 * Created by Nigel on 03/03/2016.
 */
public class FoodEnt {
    public String date, mealNum;
    public double carbs, fats, protein, calories;

    public FoodEnt(){

    }
    public String getDate(){
        return date;
    }

    public String getMealNum(){
        return mealNum;
    }
    public double getCarbs(){
        return carbs;
    }
    public double getFats(){
        return fats;
    }
    public double getProtein(){
        return protein;
    }
    public double getCalories(){
        return calories;
    }

    public void setDate(String date){
        this.date = date;
    }
    public void setMealNum(String mealNum){
        this.mealNum = mealNum;
    }
    public void setCarbs(double carbs){
       this.carbs = carbs;
    }
    public void setFats(double fats){
        this.fats = fats;
    }
    public void setProtein(double protein){
        this.protein = protein;
    }
    public void setCalories(double calories){
        this.calories = calories;
    }






}
