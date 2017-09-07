package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class viewPastMeals extends Activity {

    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    foodEntryDbAdapter helper = new foodEntryDbAdapter(this);
    SessionManager session;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_meals);
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

    }
    public void searchMealClick(View v){
        EditText date = (EditText) findViewById(R.id.date);
        String datestr = date.getText().toString();

        List<Meals> mealsList = getAllMeals(datestr,email);
        if(mealsList.size()!=0) {
            ListView lv = (ListView) findViewById(R.id.pastMeals);
            ArrayAdapter<Meals> arrayAdapter = new ArrayAdapter<>(this, R.layout.listviewfontsize, mealsList);
            lv.setAdapter(arrayAdapter);
           // Intent i = new Intent(viewPastMeals.this, viewPastMeals.class);
            //startActivity(i);

        }

    }
    public List<Meals> getAllMeals(String date,String userEmail){
        List<Meals> meals = new ArrayList<>();

        String mealDayQuery = "select mealName, mealCals, mealCarbs, mealFats, mealProtein from meals where mealDate = \""+date+"\" and mealUser = \""+userEmail+"\" ;";

        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(mealDayQuery, null);
        if(c.moveToFirst()){
            do{
                Meals m = new Meals();
                m.setMealName(c.getString(c.getColumnIndex(helper.MEALS_MEALNAME)));
                m.setCalories(c.getDouble(c.getColumnIndex(helper.MEALS_CALS)));
                m.setCarbs(c.getDouble(c.getColumnIndex(helper.MEALS_CARBS)));
                m.setFats(c.getDouble(c.getColumnIndex(helper.MEALS_FATS)));
                m.setProtein(c.getDouble(c.getColumnIndex(helper.MEALS_PROTEIN)));
                meals.add(m);
            }while(c.moveToNext());
        }
        return meals;

    }
    public void onBackPressed(){
        Intent i = new Intent(viewPastMeals.this, addMeal.class);
        startActivity(i);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_editpersonal:
                editPersonal();
                return true;
            case R.id.action_home:
                homepage();
                return true;
            case R.id.action_logout:
                logoutPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void editPersonal(){
        Intent i = new Intent(viewPastMeals.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(viewPastMeals.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(viewPastMeals.this, MainActivity_Homepage.class);
        startActivity(y);
    }

}
