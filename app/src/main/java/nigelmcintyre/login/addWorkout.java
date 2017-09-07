package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class addWorkout extends Activity {

    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss",Locale.ENGLISH);
    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    excerciseDbAdapter dbAdapter = new excerciseDbAdapter(this);
    SessionManager session;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        db = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM workout";
        Cursor c = db.rawQuery(count,null);
        c.moveToFirst();
        int icount = c.getInt(0);
        if(icount>0){
            List<Workout> workoutList = getAllWorkouts(email,todaysDate);
            ListView lv = (ListView) findViewById(R.id.workoutList);
            ArrayAdapter<Workout> arrayAdapter = new ArrayAdapter<Workout>(this, R.layout.listviewfontsize,workoutList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapterView,View view, int position, long id){
                    String item = ((TextView)view).getText().toString();
                    String[] workoutName = item.split("\\ ");

                    StringBuilder strBuilder = new StringBuilder();
                    if(workoutName.length==21) {
                        for (int i = 0; i < 3; i++) {
                            strBuilder.append(workoutName[i] + " ");
                        }
                    }else if(workoutName.length==20){
                        for (int i = 0; i < 2; i++) {
                            strBuilder.append(workoutName[i] + " ");
                        }
                    }else if(workoutName.length==19){
                        for(int i=0;i<1;i++){
                            strBuilder.append(workoutName[i] + " ");
                        }
                    }
                    String newString = strBuilder.toString();
                    String strippedString = newString.trim();
                    Intent i = new Intent(addWorkout.this, addExercise.class);
                    i.putExtra("workout", strippedString);
                    startActivity(i);

                }
            });

        }
    }
    //gets all
    public List<Workout> getAllWorkouts(String email, String todaysDate){
        List<Workout> workouts = new ArrayList<>();
        String query = "SELECT workName, date FROM workout WHERE uName = \""+email+"\" AND date >= \""+todaysDate+"\"-7 ;";
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                Workout w = new Workout();
                w.setWorkoutName(c.getString(c.getColumnIndex(dbAdapter.WORKOUT_NAME)));
                w.setDate(c.getString(c.getColumnIndex(dbAdapter.WORKOUT_DATE)));
                workouts.add(w);
            }while(c.moveToNext());
        }
        return workouts;
    }


    public void addButtonClick(View v){
        EditText workoutName = (EditText)findViewById(R.id.inputWorkoutName);
        String workoutstr = workoutName.getText().toString();
        Workout w = new Workout();
        w.setWorkoutName(workoutstr);
        w.setWorkoutUser(email);
        w.setDate(todaysDate);
        w.setDuration("");
        w.setTime(timeFormat.format(date));
        new updateWorkouts().execute();
        dbAdapter.insertWorkout(w);
        Intent i = new Intent(addWorkout.this, addExercise.class);
        i.putExtra("workout", workoutstr);
        startActivity(i);

    }
    public void onBackPressed(){
        Intent i = new Intent(addWorkout.this, MainActivity_Homepage.class);
        startActivity(i);

    }
    public void viewWorkClick(View v){
        Intent i = new Intent(addWorkout.this, viewPastWorkouts.class);
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
        Intent i = new Intent(addWorkout.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(addWorkout.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(addWorkout.this, MainActivity_Homepage.class);
        startActivity(y);
    }
    private class updateWorkouts extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`workout` (`date`, `workName`, `uName`)";

        String workoutstr;

        protected void onPreExecute(){
            EditText workoutName = (EditText)findViewById(R.id.inputWorkoutName);
            String workoutstr = workoutName.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+" VALUES ('"+todaysDate+"', '"+workoutstr+"', '"+email+"');");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
