package nigelmcintyre.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nigel on 13/02/2016.
 */
public class DbHelper extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "XNTR.db";
    SQLiteDatabase db;
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(userDbAdapter.TABLE_CREATE);
        db.execSQL(foodEntryDbAdapter.MEALENT_CREATE);
        db.execSQL(foodEntryDbAdapter.MEALS_CREATE);
        db.execSQL(foodEntryDbAdapter.FOODS_CREATE);
        db.execSQL(excerciseDbAdapter.EXCERCISE_CREATE);
        db.execSQL(excerciseDbAdapter.EXENTRY_CREATE);
        db.execSQL(excerciseDbAdapter.WORKOUT_CREATE);
        this.db = db;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("taskDBAdapter", "Upgrading from version" +oldVersion+ "to" +newVersion+ "which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS" + "TEMPLATE");
        this.onCreate(db);
    }



}
