package nigelmcintyre.login;

import java.util.Date;

/**
 * Created by Nigel on 03/03/2016.
 */
public class MealEnt {
    public String date,time,foodName,mealNum;
    public int MealFoodEntId;

    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getMealFoodEntId() {
        return MealFoodEntId;
    }

    public String getMealNum() {
        return mealNum;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMealFoodEntId(int mealFoodEntId) {
        MealFoodEntId = mealFoodEntId;
    }

    public void setMealNum(String mealNum) {
        this.mealNum = mealNum;
    }
    public void setTime(String time) {
        this.time = time;
    }


    public String toString(){
        return this.foodName;
    }
}

