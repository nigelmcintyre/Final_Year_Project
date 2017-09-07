package nigelmcintyre.login;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.lang.StrictMath.rint;

public class MainActivity_Homepage extends Activity {

    private
    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);
    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    userDbAdapter dbAdapter = new userDbAdapter(this);
    foodEntryDbAdapter foodAdapter = new foodEntryDbAdapter(this);
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__homepage);



        session = new SessionManager(getApplicationContext());

        //get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        String password = user.get(SessionManager.KEY_PASSWORD);
        User u;
        //using email entered to as parameter for getinfo to query database for corresponding email
        u = dbAdapter.getInfo(email);
        double goalCarbs = u.getCarbs();
        double goalFats = u.getFats();
        double goalProtein = u.getProtein();
        double goalCals = u.getGoal();

        TextView calories = (TextView) findViewById(R.id.txtCalories);
        //displaying name that corresponds to email
        calories.setText(String.valueOf(goalCals));
        TextView carbs = (TextView) findViewById(R.id.txtCarbs);
        carbs.setText(String.valueOf(goalCarbs));
        TextView fats = (TextView) findViewById(R.id.txtFats);
        fats.setText(String.valueOf(goalFats));
        TextView protein = (TextView) findViewById(R.id.txtProtein);
        protein.setText(String.valueOf(goalProtein));

        db = dbHelper.getReadableDatabase();
        String count = "SELECT count(*) FROM meals";
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int icount = c.getInt(0);
        if(icount>0) {
            Meals m;

            List<Meals> mealList = getAllMeals(todaysDate,email);
            if(mealList.size()!=0) {
                double totCarbs = 0.0;
                double totFats = 0.0;
                double totProtein = 0.0;
                double totCals = 0.0;
                ArrayList<Double> carbsArray = new ArrayList<>();
                ArrayList<Double> fatsArray = new ArrayList<>();
                ArrayList<Double> proteinArray = new ArrayList<>();
                ArrayList<Double> calsArray = new ArrayList<>();

                for (int j = 0; j < mealList.size(); j++) {
                    m = foodAdapter.getMealInfo(mealList.get(j).getMealName(),todaysDate,email);
                    double currCarbs = Double.valueOf(m.getCarbs());
                    double currFats = Double.valueOf(m.getFats());
                    double currProtein = Double.valueOf(m.getProtein());
                    double currCals = Double.valueOf(m.getCalories());

                    totCarbs = currCarbs + (mealList.get(j).getCarbs());
                    totFats = currFats + (mealList.get(j).getFats());
                    totProtein = currProtein + (mealList.get(j).getProtein());
                    totCals = currCals + (mealList.get(j).getCalories());

                    carbsArray.add(rint(totCarbs));
                    fatsArray.add(rint(totFats));
                    proteinArray.add(rint(totProtein));
                    calsArray.add(rint(totCals));

                }
                double sumOfCarbs = sum(carbsArray);
                double sumOfFats = sum(fatsArray);
                double sumOfProtein = sum(proteinArray);
                double sumOfCals = sum(calsArray);

                double updatedCarbs = goalCarbs - sumOfCarbs;
                double updatedFats = goalFats - sumOfFats;
                double updatedProtein = goalProtein - sumOfProtein;
                double updatedCals = goalCals - sumOfCals;


                calories.setText(String.valueOf(rint(updatedCals)));

                carbs.setText(String.valueOf(rint(updatedCarbs)));

                fats.setText(String.valueOf(rint(updatedFats)));

                protein.setText(String.valueOf(rint(updatedProtein)));
            }
        }

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public double sum(ArrayList<Double> arrayList){
        double sum=0.0;
        for(double i:arrayList)
            sum = sum +i;
        return sum;
    }
    public List<Meals> getAllMeals(String todaysDate, String email){
        List<Meals> meals = new ArrayList<Meals>();

        String mealDayQuery = "select mealName from meals where mealDate = \""+todaysDate+"\" AND mealUser = \""+email+"\";";

        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(mealDayQuery, null);
        if(c.moveToFirst()){
            do{
                Meals m = new Meals();
                m.setMealName(c.getString(c.getColumnIndex(foodAdapter.MEALS_MEALNAME)));
                meals.add(m);
            }while(c.moveToNext());
        }
        return meals;

    }
    public void addButtonClick(View v){
        if (v.getId() == R.id.btnAddFood){

            Intent i = new Intent(MainActivity_Homepage.this, addMeal.class);

            startActivity(i);
        }
        else{
            Intent i = new Intent(MainActivity_Homepage.this, addWorkout.class);

            startActivity(i);
        }
    }
    public void onBackPressed(){
        Intent i = new Intent(MainActivity_Homepage.this, MainActivity_Homepage.class);
        startActivity(i);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_editpersonal:
                editPersonal();
                return true;
            case R.id.action_logout:
                logoutPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void editPersonal(){
        Intent i = new Intent(MainActivity_Homepage.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(MainActivity_Homepage.this, Login.class);
        startActivity(x);

    }
}
