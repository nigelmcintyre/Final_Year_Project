package nigelmcintyre.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.lang.StrictMath.rint;

public class addNewFood extends Activity {
    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);
    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    foodEntryDbAdapter dbAdapter = new foodEntryDbAdapter(this);
    Intent i;
    String mealName;
    SessionManager session;
    String email;
    final Context context = this;
    double quantityAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        i = getIntent();
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        mealName = i.getStringExtra("mealName");


    }

    public void onAddFoodClicked(View v) {
        //-------------pop up to allow user to input quantity of food -------------------------------------------------------------------
        LayoutInflater li = LayoutInflater.from(context);
        View promptView = li.inflate(R.layout.quantityprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final EditText userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int d) {
                quantityAmt =  Double.valueOf(String.valueOf(userInput.getText()));
                EditText name = (EditText) findViewById(R.id.inputFoodName);
                EditText carbs = (EditText) findViewById(R.id.inputFoodCarbs);
                EditText fats = (EditText) findViewById(R.id.inputFoodFats);
                EditText protein = (EditText) findViewById(R.id.inputFoodProtein);

                String namestr = name.getText().toString();
                double carbsstr = Double.parseDouble(carbs.getText().toString());
                double fatsstr = Double.parseDouble(fats.getText().toString());
                double proteinstr = Double.parseDouble(protein.getText().toString());

                Foods f = new Foods();
                double calories = ((carbsstr * 4) + (fatsstr * 9) + (proteinstr * 4));
                f = dbAdapter.getFoodInfo(namestr);
                String existingFood = f.getFoodName();
                //check to see if food already exists
                if (existingFood == null) {
                    f.setCarbs(carbsstr);
                    f.setProtein(proteinstr);
                    f.setFat(fatsstr);
                    f.setFoodName(namestr);
                    f.setCalories(calories);
                    dbAdapter.insertFood(f);
                    new updateFoods().execute();
                    MealEnt me = new MealEnt();
                    me.setMealNum(mealName);
                    me.setFoodName(namestr);
                    me.setDate(todaysDate);
                    me.setTime(timeFormat.format(date));
                    dbAdapter.insertMealEnt(me);
                    new updateMealEnts().execute();
                    Meals m;
                    m = dbAdapter.getMealInfo(mealName, todaysDate,email);
                    String mealsDate = String.valueOf(m.date);
                    String currMealName = String.valueOf(m.mealName);
                    //check if meal we're adding food info to is the right meal
                    if (mealsDate.equals(todaysDate) && currMealName.equals(mealName)) {

                        //adding calories to meal
                        double currentCarbs = Double.valueOf(m.getCarbs());
                        double currentFats = Double.valueOf(m.getFats());
                        double currentProtein = Double.valueOf(m.getProtein());
                        double currentCalories = Double.valueOf(m.getCalories());
                        double quantCarbs = carbsstr*quantityAmt;
                        double quantFats = fatsstr*quantityAmt;
                        double quantProtein = proteinstr*quantityAmt;
                        double quantCalories = calories*quantityAmt;
                        double newcarbs = currentCarbs + quantCarbs;
                        double newfats = currentFats + quantFats;
                        double newprotein = currentProtein + quantProtein;
                        double newcalories = currentCalories + quantCalories;
                        m.setCarbs(rint(newcarbs));
                        m.setFats(rint(newfats));
                        m.setProtein(rint(newprotein));
                        m.setCalories(rint(newcalories));
                        m.setMealName(mealName);
                        m.setMealUser(email);
                        dbAdapter.updateMeal(m);
                        new updateMeals().execute();
                        Toast.makeText(addNewFood.this, "Food added to meal", Toast.LENGTH_SHORT).show();
                        Intent x = new Intent(addNewFood.this, addFood.class);

                        x.putExtra("mealName", mealName);
                        startActivity(x);

                    }
                }   else
                    Toast.makeText(addNewFood.this, "Food already exists", Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//---------------------------------------------------------------------------------------------------------------------------


    }
    public void onBackPressed(){
        Intent i = new Intent(addNewFood.this, addFood.class);
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
        Intent i = new Intent(addNewFood.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(addNewFood.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(addNewFood.this, MainActivity_Homepage.class);
        startActivity(y);
    }
    private class updateMealEnts extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`mealEnt` (`mealEntId`, `mealEntDate`, `mealEntTime`, `mealEntFood`, `mealEntMealNum`)";
        private String namestr;
        @Override
        protected void onPreExecute(){
            EditText name = (EditText) findViewById(R.id.inputFoodName);
            namestr = name.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('', '"+todaysDate+"', '"+timeFormat.format(date)+"', '"+namestr+"', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class updateMeals extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealCals`, `mealId`, `mealUser`)";
        private double newCarbs, newFats, newProteins, newCals;

        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+todaysDate+"', '"+timeFormat.format(date)+"', '"+mealName+"', '"+newCarbs+"', '"+newFats+"', '"+newProteins+"', '"+newCals+"', '', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class updateFoods extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`foods` (`foodName`, `foodFat`, `foodCarbs`, `foodProtein`, `foodCal`, `foodsId`)";
        String namestr;
        double carbsstr;
        double fatsstr;
        double proteinstr;
        double calories;

        protected void onPreExecute(){
            EditText name = (EditText) findViewById(R.id.inputFoodName);
            EditText carbs = (EditText) findViewById(R.id.inputFoodCarbs);
            EditText fats = (EditText) findViewById(R.id.inputFoodFats);
            EditText protein = (EditText) findViewById(R.id.inputFoodProtein);

            namestr = name.getText().toString();
            carbsstr = Double.parseDouble(carbs.getText().toString());
            fatsstr = Double.parseDouble(fats.getText().toString());
            proteinstr = Double.parseDouble(protein.getText().toString());
            calories = ((carbsstr * 4) + (fatsstr * 9) + (proteinstr * 4));
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+namestr+"', '"+fatsstr+"', '"+carbsstr+"', '"+proteinstr+"', '"+calories+"', '1');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

