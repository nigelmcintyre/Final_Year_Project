package nigelmcintyre.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class foodEntryDbAdapter {



    public static final String MEALS_NAME = "meals";
    public static final String MEALS_ID = "mealId";
    public static final String MEALS_USER = "mealUser";
    public static final String MEALS_MEALNAME ="mealName";
    public static final String MEALS_CARBS = "mealCarbs";
    public static final String MEALS_FATS = "mealFats";
    public static final String MEALS_PROTEIN = "mealProtein";
    public static final String MEALS_TIME = "mealTime";
    public static final String MEALS_DATE = "mealDate";
    public static final String MEALS_CALS = "mealCals";

    public static final String MEALENT_NAME = "mealEnt";
    public static final String MEALENT_ID = "mealEntId";
    public static final String MEALENT_TIME = "mealEntTime";
    public static final String MEALENT_DATE = "mealEntDate";
    public static final String MEALENT_FOODNAME = "mealEntFood";
    public static final String MEALENT_MEALNUM = "mealEntMealNum";

    public static final String FOODS_TABLE = "foods";
    public static final String FOODS_NAME = "foodName";
    public static final String FOODS_FAT ="foodFat";
    public static final String FOODS_CARBS = "foodCarbs";
    public static final String FOODS_PROTEIN = "foodProtein";
    public static final String FOODS_CAL = "foodCal";
    public static final String FOODS_ID = "foodsId";

    SQLiteDatabase db;
    private final Context context;
    private DbHelper dbHelper;

    static final String MEALS_CREATE = "create table meals(mealDate datetime not null ,mealTime text not null, mealName text not null, " +
            "mealCarbs double not null, mealFats double not null, mealProtein double not null, mealCals double not null, mealId integer primary key autoincrement, mealUser text not null); ";

    static final String MEALENT_CREATE = "create table mealEnt(mealEntId integer primary key autoincrement, mealEntDate datetime not null, mealEntTime datetime not null, mealEntFood text not null, " +
            "mealEntMealNum text not null); ";

    static final String FOODS_CREATE = "create table foods(foodName text primary key, foodFat double not null, " +
            "foodCarbs double not null, foodProtein double not null, foodCal double not null, foodsId integer not null); ";

    public foodEntryDbAdapter(Context _context){
        context = _context;
        dbHelper = new DbHelper(context);
    }


    public void insertMeal (Meals m){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from meals ";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(MEALS_ID, count);
        values.put(MEALS_MEALNAME, m.getMealName());
        values.put(MEALS_CALS, m.getCalories());
        values.put(MEALS_CARBS, m.getCarbs());
        values.put(MEALS_FATS, m.getFats());

        values.put(MEALS_PROTEIN, m.getProtein());
        values.put(MEALS_DATE, m.getDate());
        values.put(MEALS_TIME, m.getTime());
        values.put(MEALS_USER, m.getMealUser());

        db.insert(MEALS_NAME, null, values);
        db.close();

    }
    //gets meal info for current user, day and meal.
    public Meals getMealInfo(String mealNum,String todaysDate,String mealUser){
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT "+ MEALS_MEALNAME +" , "+
                MEALS_CALS+" , "+MEALS_TIME+" , "+MEALS_CARBS+" , "+MEALS_FATS+
                " , "+MEALS_PROTEIN+" , "+MEALS_DATE+" , "+MEALS_USER+" FROM "+
                MEALS_NAME +" WHERE "+MEALS_MEALNAME+" =? and "+MEALS_DATE+" =? and "+MEALS_USER+" =?";


        Meals m = new Meals();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(mealNum),String.valueOf(todaysDate),String.valueOf(mealUser)});

        if(cursor.moveToFirst()){
            do{
                m.calories = cursor.getDouble(cursor.getColumnIndex(MEALS_CALS));
                m.mealName = cursor.getString(cursor.getColumnIndex(MEALS_MEALNAME));
                m.carbs = cursor.getDouble(cursor.getColumnIndex(MEALS_CARBS));
                m.fats = cursor.getDouble(cursor.getColumnIndex(MEALS_FATS));
                m.protein = cursor.getDouble(cursor.getColumnIndex(MEALS_PROTEIN));
                m.date = cursor.getString(cursor.getColumnIndex(MEALS_DATE));
                m.time = cursor.getString(cursor.getColumnIndex(MEALS_TIME));
                m.mealUser = cursor.getString(cursor.getColumnIndex(MEALS_USER));

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return m;

    }
    public void deleteMeal(String mealName,String todaysDate){
        db = dbHelper.getWritableDatabase();
        db.delete(MEALS_NAME, MEALS_MEALNAME + "= ? AND "+MEALS_DATE+" = ?", new String[]{String.valueOf(mealName),String.valueOf(todaysDate)
        });
        db.close();
    }
    public void updateMeal(Meals m){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEALS_CARBS, m.getCarbs());
        values.put(MEALS_PROTEIN, m.getProtein());
        values.put(MEALS_FATS, m.getFats());
        values.put(MEALS_CALS, m.getCalories());



        db.update(MEALS_NAME, values, "mealName =? and mealDate =? and  mealUser = ?", new String[]{m.getMealName(), m.getDate(), m.getMealUser()});
        db.close();
    }

    public void insertMealEnt (MealEnt m){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from mealEnt ";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(MEALENT_FOODNAME, m.getFoodName());
        values.put(MEALENT_MEALNUM, m.getMealNum());
        values.put(MEALENT_DATE, m.getDate());
        values.put(MEALENT_TIME, m.getTime());

        db.insert(MEALENT_NAME, null, values);
        db.close();

    }
    public void deleteMealEntFood(String foodName, String mealName, String todaysDate){
        db = dbHelper.getWritableDatabase();
        db.delete(MEALENT_NAME, MEALENT_FOODNAME + "= ? and " + MEALENT_MEALNUM + "=? and " + MEALENT_DATE + "=?", new String[]{String.valueOf(foodName), String.valueOf(mealName), String.valueOf(todaysDate)
        });
        db.close();
    }
    public void deleteMealEntMeal(String mealName,String todaysDate){
        db = dbHelper.getWritableDatabase();
        db.delete(MEALENT_NAME, MEALENT_MEALNUM + "= ? AND " + MEALENT_DATE + "=?" , new String[]{String.valueOf(mealName),String.valueOf(todaysDate)
        });
        db.close();
    }

    public void insertFood (Foods f){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from foods ";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(FOODS_ID, count);
        values.put(FOODS_NAME, f.getFoodName());
        values.put(FOODS_CAL, f.getCalories());
        values.put(FOODS_CARBS, f.getCarbs());
        values.put(FOODS_FAT, f.getFat());
        values.put(FOODS_PROTEIN, f.getProtein());


        db.insert(FOODS_TABLE, null, values);
        db.close();

    }
    public Foods getFoodInfo(String foodName){
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT "+ FOODS_NAME +" , "+
                FOODS_CARBS+" , "+FOODS_CAL+" , "+FOODS_FAT+
                " , "+FOODS_PROTEIN+" FROM "+ FOODS_TABLE +" WHERE "+FOODS_NAME+" =?";


        Foods f = new Foods();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(foodName)});

        if(cursor.moveToFirst()){
            do{
                f.calories = cursor.getDouble(cursor.getColumnIndex(FOODS_CAL));
                f.FoodName = cursor.getString(cursor.getColumnIndex(FOODS_NAME));
                f.carbs = cursor.getDouble(cursor.getColumnIndex(FOODS_CARBS));
                f.fat = cursor.getDouble(cursor.getColumnIndex(FOODS_FAT));
                f.protein = cursor.getDouble(cursor.getColumnIndex(FOODS_PROTEIN));

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return f;

    }
    public void updateFood(Foods f){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FOODS_CARBS, f.getCarbs());
        values.put(FOODS_PROTEIN, f.getProtein());
        values.put(FOODS_FAT, f.getFat());
        values.put(FOODS_CAL, f.getCalories());

        db.update(FOODS_TABLE, values, "foodName =?", new String[]{f.getFoodName()});
        db.close();
    }
    public void deleteFood(String foodName){
        db = dbHelper.getWritableDatabase();
        db.delete(FOODS_TABLE, FOODS_NAME + "= ?", new String[]{String.valueOf(foodName)
        });
        db.close();
    }

}