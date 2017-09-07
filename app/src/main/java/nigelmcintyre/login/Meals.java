package nigelmcintyre.login;

import java.sql.Date;

/**
 * Created by Nigel on 03/03/2016.
 */
public class Meals {

    public String  mealName,date, time,mealUser;
    public double carbs, fats, protein, calories;

    public Meals(){

    }
    public String getMealUser(){
        return mealUser;
    }
    public String getMealName(){
        return mealName;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }

    public double getCalories() {
        return calories;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public double getProtein() {
        return protein;
    }
    public void setMealName(String mealName){
        this.mealName = mealName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setMealUser(String mealUser){
        this.mealUser = mealUser;
    }
    public String toString(){
        return this.mealName+"  "+this.calories+"cal  "+this.carbs+"c  "+this.fats+"f  "+this.protein+"p";
    }
}


