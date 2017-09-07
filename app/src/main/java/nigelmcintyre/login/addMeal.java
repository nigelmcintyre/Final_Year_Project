package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.PriorityQueue;

import static java.lang.StrictMath.rint;

public class addMeal extends Activity {

    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss",Locale.ENGLISH);

    foodEntryDbAdapter helper = new foodEntryDbAdapter(this);
    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    Intent i;
    SessionManager session;
    String email;
    private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355mn&password=qa0tol";
    private Connection connection = null;
    private Statement statement =  null;
    private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealkCals`, `mealId`, `mealUser`)";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        i = getIntent();

        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        db = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM meals";
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int icount = c.getInt(0);
        if(icount>0){
            List<Meals> mealsList = getAllMeals(todaysDate,email);
            ListView lv = (ListView) findViewById(R.id.listMeals);
            ArrayAdapter<Meals> arrayAdapter = new ArrayAdapter<Meals>(this, R.layout.listviewfontsize,mealsList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String item = ((TextView) view).getText().toString();
                    String[] mealName = item.split("\\ ");
                    i = new Intent(addMeal.this, addFood.class);
                    i.putExtra("mealName", mealName[0]);
                    startActivity(i);
                }
            });

        }

    }

    public List<Meals> getAllMeals(String todaysDate,String userEmail){
        List<Meals> meals = new ArrayList<Meals>();

        String mealDayQuery = "select mealName, mealCals, mealCarbs, mealFats, mealProtein from meals where mealDate = \""+todaysDate+"\" and mealUser = \""+userEmail+"\" ;";

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
    public void onDeleteClick(View v){
        EditText mealName = (EditText)findViewById(R.id.inputDeleteMeal);
        String mealNamestr = mealName.getText().toString();
        helper.deleteMeal(mealNamestr,todaysDate);
        helper.deleteMealEntMeal(mealNamestr,todaysDate);
        Intent i = new Intent(addMeal.this, addMeal.class);
        startActivity(i);
    }

    public void addFoodButtonClick(View v){

        EditText mealName = (EditText)findViewById(R.id.inputMealName);
        String mealNamestr = mealName.getText().toString();

        Meals m = new Meals();
        String query = "select mealName from meals where mealDate = \""+todaysDate+"\";";
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        ArrayList<String> checkMealName = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                String tempMealName = c.getString(c.getColumnIndex(helper.MEALS_MEALNAME));
                checkMealName.add(tempMealName);
            }while(c.moveToNext());
        }
        if(checkMealName.size()==0){
            m.setMealName(mealNamestr);
            m.setDate(todaysDate);
            m.setTime(timeFormat.format(date));
            m.setCarbs(0);
            m.setProtein(0);
            m.setFats(0);
            m.setCalories(0);
            m.setMealUser(email);

            helper.insertMeal(m);

            new updateDatabase().execute();

            i = new Intent(addMeal.this, addFood.class);
            i.putExtra("mealName", mealNamestr);
            startActivity(i);
        }else{
            for(int j=0;j<checkMealName.size();j++){

                if(checkMealName.get(j).equals(mealNamestr)){
                    Toast temp = Toast.makeText(addMeal.this, "Meal name already exists", Toast.LENGTH_SHORT);
                    temp.show();
                    j=checkMealName.size();
                }else if (j==checkMealName.size()-1){
                    m.setMealName(mealNamestr);
                    m.setDate(todaysDate);
                    m.setTime(timeFormat.format(date));
                    m.setCarbs(0);
                    m.setProtein(0);
                    m.setFats(0);
                    m.setCalories(0);
                    m.setMealUser(email);

                    helper.insertMeal(m);
                    new updateDatabase().execute();


                    i = new Intent(addMeal.this, addFood.class);
                    i.putExtra("mealName", mealNamestr);
                    startActivity(i);

                }
            }
        }

    }
    public void viewPastClick(View v){
        Intent i = new Intent(addMeal.this, viewPastMeals.class);
        startActivity(i);
    }
    public void onBackPressed(){
        Intent i = new Intent(addMeal.this, MainActivity_Homepage.class);
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
        Intent i = new Intent(addMeal.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(addMeal.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(addMeal.this, MainActivity_Homepage.class);
        startActivity(y);
    }
    private class updateDatabase extends AsyncTask<Void, Void, Void>{
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealCals`, `mealId`, `mealUser`)";
        private String mealNamestr;
        @Override
        protected void onPreExecute(){
            EditText mealName = (EditText)findViewById(R.id.inputMealName);
            mealNamestr = mealName.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
               Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+todaysDate+"', '"+timeFormat.format(date)+"', '"+mealNamestr+"', '0', '0', '0', '0', '0', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}



