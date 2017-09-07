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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class addFood extends Activity {
    SQLiteDatabase db;
    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);
    DbHelper dbHelper= new DbHelper(this);
    foodEntryDbAdapter dbAdapter = new foodEntryDbAdapter(this);
    Foods f = new Foods();
    Meals m;
    Intent i;
    String mealName;
    final Context context = this;
    double quantityAmt;

    SessionManager session;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        TextView name = (TextView) findViewById(R.id.txtMealName);
        i = getIntent();
        mealName = i.getStringExtra("mealName");
        name.setText(mealName);
        //list of mealEnts
        db = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM mealEnt, foods";
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int icount = c.getInt(0);
        if(icount>0){
            List<MealEnt> mealEntList = getAllFoodsForMeal(mealName,todaysDate);
            ListView lv = (ListView) findViewById(R.id.foodInMeal);
            //display list in a list view
            ArrayAdapter<MealEnt> arrayAdapter = new ArrayAdapter<MealEnt>(this, R.layout.listviewfontsize, mealEntList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                //deletes food item from mealEnt
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//----------------------adds food to the meal and add nutritional info to meal ----------------------------------------------
                    String foodNamestr = ((TextView) view).getText().toString();

                    dbAdapter.deleteMealEntFood(foodNamestr, mealName, todaysDate);

                    f = dbAdapter.getFoodInfo(foodNamestr);
                    double foodCarbs = Double.valueOf(f.carbs);
                    double foodFats = Double.valueOf(f.fat);
                    double foodProtein = Double.valueOf(f.protein);
                    double foodCalories = Double.valueOf(f.calories);

                    m = dbAdapter.getMealInfo(mealName, todaysDate, email);
                    double mealCarbs = Double.valueOf(m.carbs);
                    double mealFats = Double.valueOf(m.fats);
                    double mealProtein = Double.valueOf(m.protein);
                    double mealCal = Double.valueOf(m.calories);
                    double newCarbs = mealCarbs - foodCarbs;
                    double newFats = mealFats - foodFats;
                    double newProtein = mealProtein - foodProtein;
                    double newCalories = mealCal - foodCalories;
                    m.setCarbs(rint(newCarbs));
                    m.setFats(rint(newFats));
                    m.setProtein(rint(newProtein));
                    m.setCalories(rint(newCalories));
                    m.setMealName(mealName);
                    m.setMealUser(email);
                    dbAdapter.updateMeal(m);
                    new updateMeals().execute();

                    final Intent g = new Intent(addFood.this, addFood.class);

                    g.putExtra("mealName", mealName);

                    startActivity(g);
                }
            });
//-------------------------------------------------------------------------------------------------------------------------
//----------- Displaying meals nutritional info ---------------------------------------------------------------------------
            m = dbAdapter.getMealInfo(mealName,todaysDate,email);
            double currentCarbs = Double.valueOf(m.carbs);
            double currentFats = Double.valueOf(m.fats);
            double currentProtein = Double.valueOf(m.protein);
            double currentCalories = Double.valueOf(m.calories);
            TextView carbs = (TextView)findViewById(R.id.totalCarbs);
            TextView fats = (TextView)findViewById(R.id.totalFats);
            TextView protein = (TextView)findViewById(R.id.totalProtein);
            TextView cals = (TextView)findViewById(R.id.totCals);
            carbs.setText(String.valueOf(currentCarbs));
            fats.setText(String.valueOf(currentFats));
            protein.setText(String.valueOf(currentProtein));
            cals.setText(String.valueOf(currentCalories));

        }
        m = dbAdapter.getMealInfo(mealName,todaysDate,email);
        double currentCarbs = Double.valueOf(m.carbs);
        double currentFats = Double.valueOf(m.fats);
        double currentProtein = Double.valueOf(m.protein);
        double currentCalories = Double.valueOf(m.calories);
        TextView carbs = (TextView)findViewById(R.id.totalCarbs);
        TextView fats = (TextView)findViewById(R.id.totalFats);
        TextView protein = (TextView)findViewById(R.id.totalProtein);
        TextView cals = (TextView)findViewById(R.id.totCals);
        carbs.setText(String.valueOf(currentCarbs));
        fats.setText(String.valueOf(currentFats));
        protein.setText(String.valueOf(currentProtein));
        cals.setText(String.valueOf(currentCalories));
    }
//-------------------------------------------------------------------------------------------------------------------------

    public List<MealEnt> getAllFoodsForMeal(String mealName,String todaysDate){
        List<MealEnt> mealEnts = new ArrayList<>();

        String joinQuery = "select * from mealEnt me, foods f, meals m where me.mealEntFood = f.foodName and m.mealUser = \""+email+"\" " +
                "and m.mealName = me.mealEntMealNum and me.mealEntDate = m.mealDate and me.mealEntMealNum = \""+mealName+"\" and m.mealDate = \""+todaysDate+"\";";
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(joinQuery, null);

        if(cursor.moveToFirst()){
            do{
                MealEnt me = new MealEnt();
                me.setFoodName(cursor.getString(cursor.getColumnIndex(dbAdapter.MEALENT_FOODNAME)));
               // me.setMealNum(cursor.getString(cursor.getColumnIndex(dbAdapter.MEALENT_FOODNAME)));
                mealEnts.add(me);
            }while(cursor.moveToNext());
        }
        return mealEnts;
    }
    public List<Foods> getFoodInfoForMeal(String mealName, String todaysDate){
        List<Foods> foodInfo = new ArrayList<>();
        String query = "SELECT  foodCarbs, foodProtein, foodFat, foodCal FROM foods f, mealEnt me, meals m WHERE f.foodName = me.mealEntFood AND me.mealEntMealNum = m.mealName" +
                " AND m.mealName = \""+mealName+"\" AND m.mealDate = me.mealEntDate AND m.mealDate = \""+todaysDate+"\" AND m.mealUser = \""+email+"\";";
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Foods f = new Foods();
                f.setCarbs(c.getDouble(c.getColumnIndex(dbAdapter.FOODS_CARBS)));
                f.setFat(c.getDouble(c.getColumnIndex(dbAdapter.FOODS_FAT)));
                f.setProtein(c.getDouble(c.getColumnIndex(dbAdapter.FOODS_PROTEIN)));
                f.setCalories(c.getDouble(c.getColumnIndex(dbAdapter.FOODS_CAL)));
                foodInfo.add(f);
            }while(c.moveToNext());
        }
        return foodInfo;
    }

    public void addNewFood(View v){
        Intent i = new Intent(addFood.this, addNewFood.class);
        i.putExtra("mealName",mealName);
        startActivity(i);
    }
    public void searchButtonClicked(View v){

        EditText searchFood = (EditText)findViewById(R.id.searchFoods);
        String searchFoodstr = searchFood.getText().toString();
        f = dbAdapter.getFoodInfo(searchFoodstr);
        TextView displayFood = (TextView)findViewById(R.id.tvSearchedFood);
        TextView foodCarbs = (TextView)findViewById(R.id.tvCarbs);
        TextView foodFats = (TextView)findViewById(R.id.tvFats);
        TextView foodProtein = (TextView)findViewById(R.id.tvProtein);
        String foodFound = String.valueOf(f.FoodName);
        if(searchFoodstr.equals(foodFound)){
            displayFood.setText(String.valueOf(f.FoodName));
            foodCarbs.setText(String.valueOf(f.carbs));
            foodFats.setText(String.valueOf(f.fat));
            foodProtein.setText(String.valueOf(f.protein));
        }
        else {
            Toast.makeText(addFood.this, "Food not in database", Toast.LENGTH_SHORT).show();
        }
    }
    public void searchedFoodClicked(View v){

//-------------pop up to allow user to input quantity of food -------------------------------------------------------------------
        LayoutInflater li = LayoutInflater.from(context);
        View promptView = li.inflate(R.layout.quantityprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final EditText userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int d) {
                quantityAmt = Double.valueOf(String.valueOf(userInput.getText()));
                EditText searchFood = (EditText)findViewById(R.id.searchFoods);
                String searchFoodstr = searchFood.getText().toString();
                i = getIntent();
                mealName = i.getStringExtra("mealName");

                MealEnt me = new MealEnt();
                me.setMealNum(mealName);
                me.setFoodName(searchFoodstr);
                me.setDate(todaysDate);
                me.setTime(timeFormat.format(date));

                dbAdapter.insertMealEnt(me);
                new updateMealEnts().execute();

                //Adding foods nutritional values to the meal
                double carbs = 0.0;
                double fats = 0.0;
                double protein = 0.0;
                double calories = 0.0;
                List<Foods> foodsList = getFoodInfoForMeal(mealName, todaysDate);

                for(int j = foodsList.size()-1;j<foodsList.size();j++){
                    Meals m;
                    m = dbAdapter.getMealInfo(mealName,todaysDate,email);
                    String mealsDate = String.valueOf(m.date);
                    String currMealName = String.valueOf(m.mealName);
                    //check if meal we're adding food info to is the right meal
                    if(mealsDate.equals(todaysDate)&&currMealName.equals(mealName)) {

                        //adding calories to meal
                        double currentCarbs = Double.valueOf(m.getCarbs());
                        double currentFats = Double.valueOf(m.getFats());
                        double currentProtein = Double.valueOf(m.getProtein());
                        double currentCalories = Double.valueOf(m.getCalories());
                        double quantCarbs = (foodsList.get(j).getCarbs())*quantityAmt;
                        double quantFats = (foodsList.get(j).getFat())*quantityAmt;
                        double quantProtein = (foodsList.get(j).getProtein())*quantityAmt;
                        double quantCals = (foodsList.get(j).getCalories())*quantityAmt;
                        carbs = currentCarbs + quantCarbs;
                        fats = currentFats + quantFats;
                        protein = currentProtein + quantProtein;
                        calories = currentCalories + quantCals;
                        m.setCarbs(rint(carbs));
                        m.setFats(rint(fats));
                        m.setProtein(rint(protein));
                        m.setCalories(rint((calories)));
                        m.setMealName(mealName);
                        m.setMealUser(email);
                        dbAdapter.updateMeal(m);
                        new updateMeals2().execute();

                        Intent i = new Intent(addFood.this, addFood.class);
                        i.putExtra("mealName", mealName);
                        mealName = i.getStringExtra("mealName");

                        startActivity(i);
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int d) {
                dialogInterface.cancel();

                startActivity(i);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//---------------------------------------------------------------------------------------------------------------------------

    }
    public void editDeleteClick(View v){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.edit_delete_food, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.inputEditFoodname);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //check if food is there
                Foods f;
                String foodName = String.valueOf(userInput.getText());
                f = dbAdapter.getFoodInfo(foodName);
                String existingFood = f.getFoodName();
                if (existingFood != null) {
                    dbAdapter.deleteFood(foodName);
                } else {
                    Toast.makeText(addFood.this, "Food not in database", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Foods f;
                String foodName = String.valueOf(userInput.getText());
                f = dbAdapter.getFoodInfo(foodName);
                String existingFood = f.getFoodName();
                if(existingFood !=null){
                    Intent i = new Intent(addFood.this, editFood.class);
                    i.putExtra("mealName", mealName);
                    startActivity(i);
                }else{
                    Toast.makeText(addFood.this, "Food not in database", Toast.LENGTH_SHORT).show();
                }

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void onBackPressed(){
        Intent i = new Intent(addFood.this, addMeal.class);
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
        Intent i = new Intent(addFood.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(addFood.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(addFood.this, MainActivity_Homepage.class);
        startActivity(y);
    }
    private class updateMeals extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealkCals`, `mealId`, `mealUser`)";
        private double newCarbs, newFats, newProteins, newCals;

        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+todaysDate+"', '"+timeFormat.format(date)+"', '"+mealName+"', '"+newCarbs+"', '"+newFats+"', '"+newProteins+"', '"+newCals+"', '1', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class updateMeals2 extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealkCals`, `mealId`, `mealUser`)";
        private double carbs, fats, proteins, cals;

        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+todaysDate+"', '"+timeFormat.format(date)+"', '"+mealName+"', '"+carbs+"', '"+fats+"', '"+proteins+"', '"+cals+"', '1', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class updateMealEnts extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`mealEnt` (`mealEntId`, `mealEntDate`, `mealEntTime`, `mealEntFood`, `mealEntMealNum`)";
        private String searchFoodstr;
        @Override
        protected void onPreExecute(){
            EditText searchFood = (EditText)findViewById(R.id.searchFoods);
            searchFoodstr = searchFood.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('1', '"+todaysDate+"', '"+timeFormat.format(date)+"', '"+searchFoodstr+"', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
