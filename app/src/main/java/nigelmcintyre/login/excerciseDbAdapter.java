package nigelmcintyre.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Nigel on 15/03/2016.
 */
public class excerciseDbAdapter {
    public static final String EXCERCISE_TABLE = "excercise";
    public static final String EXCERCISE_NAME = "name";

    public static final String EXENTRY_TABLE = "exEntry";
    public static final String EXENTRY_NUM = "exNum";
    public static final String EXENTRY_WORKOUT = "exWorkout";
    public static final String EXENTRY_REPS = "reps";
    public static final String EXENTRY_SETS = "sets";
    public static final String EXENTRY_NAME = "exName";
    public static final String EXENTRY_USERNAME ="exUName";
    public static final String EXENTRY_WEIGHT = "exWeight";
    public static final String EXENTRY_DATE = "exDate";

    public static final String WORKOUT_TABLE = "workout";
    public static final String WORKOUT_NAME = "workName";
    public static final String WORKOUT_DATE = "date";
    public static final String WORKOUT_DURATION = "duration";
    public static final String WORKOUT_UNAME = "uName";
    public static final String WORKOUT_TIME = "workoutTime";

    SQLiteDatabase db;
    private final Context c;
    private DbHelper dbHelper;

    static final String EXCERCISE_CREATE = "create table excercise(name text primary key);";

    static final String EXENTRY_CREATE = "create table exEntry(exNum integer primary key autoincrement, exWorkout text not null, reps integer not null," +
            "sets integer not null, exName text not null, exUName text not null, exWeight text not null, exDate datetime not null);";

    static final String WORKOUT_CREATE = "create table workout(workoutTime datetime primary key, date datetime not null, workName text not null, duration datetime not null, uName text not null);";

    public excerciseDbAdapter(Context _context) {
        c = _context;
        dbHelper = new DbHelper(c);
    }
    public void insertExcercise(Excercise e){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from excercise";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(EXCERCISE_NAME,e.getExcerciseName());
        db.insert(EXCERCISE_TABLE, null, values);
        db.close();
    }
    public void insertExEntry(ExEntry ex){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from exEntry ";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(EXENTRY_NAME, ex.getExName());
        values.put(EXENTRY_WORKOUT, ex.getExWorkout());
        values.put(EXENTRY_REPS, ex.getReps());
        values.put(EXENTRY_SETS, ex.getSets());
        values.put(EXENTRY_WEIGHT, ex.getWeight());
        values.put(EXENTRY_USERNAME, ex.getuName());
        values.put(EXENTRY_DATE, ex.getDate());

        db.insert(EXENTRY_TABLE, null, values);
        db.close();
    }
    public void deleteExEntry(String workoutName, String todaysDate, String userName, String excerciseName){
        db = dbHelper.getWritableDatabase();
        db.delete(EXENTRY_TABLE, EXENTRY_WORKOUT+"=? and " +EXENTRY_NAME+"=? and "+ EXENTRY_USERNAME+"=? and "+EXENTRY_NAME+"=?",  new String[]{
                String.valueOf(workoutName),String.valueOf(todaysDate),String.valueOf(userName),String.valueOf(excerciseName)});
    }
    public void insertWorkout(Workout w){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from workout";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(WORKOUT_NAME, w.getWorkoutName());
        values.put(WORKOUT_DATE, w.getDate());
        values.put(WORKOUT_DURATION, w.getDuration());
        values.put(WORKOUT_UNAME, w.getWorkoutUser());
        values.put(WORKOUT_TIME, w.getTime());


        db.insert(WORKOUT_TABLE, null, values);
        db.close();
    }

}
