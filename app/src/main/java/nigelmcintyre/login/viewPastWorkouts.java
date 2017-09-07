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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class viewPastWorkouts extends Activity {

    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    excerciseDbAdapter dbAdapter = new excerciseDbAdapter(this);
    SessionManager session;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_workouts);
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
    }
    public void searchWorkoutClick(View v){
        EditText date= (EditText) findViewById(R.id.workoutDate);
        String datestr = date.getText().toString();

        List<ExEntry> workoutList = getAllWorkouts(email, datestr);
        if(workoutList.size()!=0) {
            ExEntry ex = new ExEntry();
            ListView lv = (ListView) findViewById(R.id.pastWorkouts);
            ArrayAdapter<ExEntry> arrayAdapter = new ArrayAdapter<ExEntry>(this, R.layout.listviewfontsize, workoutList);
            lv.setAdapter(arrayAdapter);
            TextView exWorkoutName = (TextView) findViewById(R.id.tvWorkoutName);

            exWorkoutName.setText(String.valueOf(ex.exWorkout));


        }


    }
    public List<ExEntry> getAllWorkouts(String email, String date){
        List<ExEntry> workouts = new ArrayList<>();
        String query = "SELECT exWorkout, reps, sets, exName, exWeight FROM exEntry WHERE exUName = \""+email+"\" AND exDate= \""+date+"\";";
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                ExEntry ex = new ExEntry();
                ex.setSets(c.getInt(c.getColumnIndex(dbAdapter.EXENTRY_SETS)));
                ex.setExWorkout(c.getString(c.getColumnIndex(dbAdapter.EXENTRY_WORKOUT)));
                ex.setReps(c.getInt(c.getColumnIndex(dbAdapter.EXENTRY_REPS)));
                ex.setExName(c.getString(c.getColumnIndex(dbAdapter.EXENTRY_NAME)));
                ex.setWeight(c.getDouble(c.getColumnIndex(dbAdapter.EXENTRY_WEIGHT)));

                workouts.add(ex);
            }while(c.moveToNext());
        }
        return workouts;
    }
    public void onBackPressed(){
        Intent i = new Intent(viewPastWorkouts.this, addWorkout.class);
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
        Intent i = new Intent(viewPastWorkouts.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(viewPastWorkouts.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(viewPastWorkouts.this, MainActivity_Homepage.class);
        startActivity(y);
    }
}
