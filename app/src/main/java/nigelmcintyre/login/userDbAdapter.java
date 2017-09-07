package nigelmcintyre.login;

/**
 * Created by Nigel on 24/02/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class userDbAdapter {


    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_BMR = "bmr";
    public static final String COLUMN_TDEE = "tdee";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_ACTIVITYLEVEL = "activity";
    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_FATS = "fats";
    public static final String COLUMN_PROTEIN = "protein";

    SQLiteDatabase db;
    private final Context context;
    private DbHelper dbHelper;
    static final String TABLE_CREATE = "create table users (id integer primary key not null  ," +
            "name text not null, surname text not null, email text not null, password text not null, sex text not null," +
            " bmr double not null, tdee double not null, height double not null, weight double not null, age integer not null," +
            "activity double not null, goal double not null, carbs double not null, fats double not null, protein double not null);";

    public userDbAdapter(Context _context){
        context = _context;
        dbHelper = new DbHelper(context);

    }

    public void insertUser(User u) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from users";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, u.getName());
        values.put(COLUMN_SURNAME, u.getSurname());
        values.put(COLUMN_EMAIL, u.getEmail());
        values.put(COLUMN_PASSWORD, u.getPassword());
        values.put(COLUMN_SEX, u.getSex());
        values.put(COLUMN_BMR, u.getBMR());
        values.put(COLUMN_TDEE, u.getTDEE());
        values.put(COLUMN_HEIGHT, u.getHeight());
        values.put(COLUMN_WEIGHT, u.getWeight());
        values.put(COLUMN_AGE, u.getAge());
        values.put(COLUMN_ACTIVITYLEVEL, u.getActivityLevel());
        values.put(COLUMN_GOAL, u.getGoal());
        values.put(COLUMN_CARBS, u.getCarbs());
        values.put(COLUMN_FATS, u.getFats());
        values.put(COLUMN_PROTEIN, u.getProtein());


        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateUser(User u){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String where = "select * from users";
        Cursor cursor = db.rawQuery(where, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, u.getName());
        values.put(COLUMN_SURNAME, u.getSurname());
        values.put(COLUMN_EMAIL, u.getEmail());
        values.put(COLUMN_PASSWORD, u.getPassword());
        values.put(COLUMN_SEX, u.getSex());
        values.put(COLUMN_BMR, u.getBMR());
        values.put(COLUMN_TDEE, u.getTDEE());
        values.put(COLUMN_HEIGHT, u.getHeight());
        values.put(COLUMN_WEIGHT, u.getWeight());
        values.put(COLUMN_AGE, u.getAge());
        values.put(COLUMN_ACTIVITYLEVEL, u.getActivityLevel());
        values.put(COLUMN_GOAL, u.getGoal());

        db.update(TABLE_NAME, values, "id = ?", new String[]{Integer.toString(count)});
        Log.d("table updated", "table updated");
        db.close();

    }

    public void updateCalUser(User u){
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SEX, u.getSex());
        values.put(COLUMN_BMR, u.getBMR());
        values.put(COLUMN_TDEE, u.getTDEE());
        values.put(COLUMN_HEIGHT, u.getHeight());
        values.put(COLUMN_WEIGHT, u.getWeight());
        values.put(COLUMN_AGE, u.getAge());
        values.put(COLUMN_ACTIVITYLEVEL, u.getActivityLevel());
        values.put(COLUMN_GOAL, u.getGoal());
        values.put(COLUMN_CARBS, u.getCarbs());
        values.put(COLUMN_FATS, u.getFats());
        values.put(COLUMN_PROTEIN, u.getProtein());

        db.update(TABLE_NAME, values, "email = ?", new String[]{u.getEmail()});
        Log.d("table updated", "table updated");
        db.close();

    }

    public User getInfo(String email){
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT "+ COLUMN_ID +" , "+
                COLUMN_NAME+" , "+COLUMN_SURNAME+" , "+COLUMN_EMAIL+
                " , "+COLUMN_PASSWORD+" , "+COLUMN_SEX+" , "+COLUMN_BMR+
                " , "+COLUMN_TDEE+" , "+COLUMN_HEIGHT+" , "+COLUMN_WEIGHT+
                " , "+COLUMN_AGE+" , "+COLUMN_ACTIVITYLEVEL+" , "+COLUMN_GOAL+
                " , "+COLUMN_CARBS+" , "+COLUMN_FATS+" , "+COLUMN_PROTEIN+
                " FROM "+ TABLE_NAME +" WHERE "+COLUMN_EMAIL+" =?";


        User u = new User();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(email)});

        if(cursor.moveToFirst()){
            do{
                u.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                u.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                u.surname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME));
                u.email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                u.password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                u.sex = cursor.getString(cursor.getColumnIndex(COLUMN_SEX));
                u.BMR = cursor.getDouble(cursor.getColumnIndex(COLUMN_BMR));
                u.TDEE = cursor.getDouble(cursor.getColumnIndex(COLUMN_TDEE));
                u.height = cursor.getDouble(cursor.getColumnIndex(COLUMN_HEIGHT));
                u.weight = cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT));
                u.age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
                u.activityLevel = cursor.getDouble(cursor.getColumnIndex(COLUMN_ACTIVITYLEVEL));
                u.goal = cursor.getDouble(cursor.getColumnIndex(COLUMN_GOAL));
                u.carbs = cursor.getDouble(cursor.getColumnIndex(COLUMN_CARBS));
                u.fats = cursor.getDouble(cursor.getColumnIndex(COLUMN_FATS));
                u.protein = cursor.getDouble(cursor.getColumnIndex(COLUMN_PROTEIN));


            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return u;

    }

        public String searchPassword(String email) {
            db = dbHelper.getReadableDatabase();
            String query = "select email, password from " +TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            String a,b;
            b = "not found";
            if(cursor.moveToFirst()){
                do{
                    a = cursor.getString(0);

                    if(a.equals(email)){
                        b = cursor.getString(1);
                        break;
                    }
                }
                while(cursor.moveToNext());
            }
            return b;
        }

    }

