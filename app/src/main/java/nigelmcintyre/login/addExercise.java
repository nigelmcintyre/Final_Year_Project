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

public class addExercise extends Activity {

    final Context context = this;
    SessionManager session;
    String email;
    String workoutName;
    SQLiteDatabase db;
    DbHelper dbHelper = new DbHelper(this);
    excerciseDbAdapter excerciseDbAdapter = new excerciseDbAdapter(this);
    java.util.Date date = new java.util.Date();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
    String todaysDate = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_exercise);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email =  user.get(SessionManager.KEY_EMAIL);
        Intent i = getIntent();
        workoutName = i.getStringExtra("workout");
        TextView workoutTitle = (TextView) findViewById(R.id.tvWorkoutName);
        workoutTitle.setText(workoutName);

        db = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM exEntry";
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int icount = c.getInt(0);
        //check if database is empty
        if(icount>0){
            List<ExEntry> exEntryList = getExcercisesForWorkout(workoutName, todaysDate, email);
            ListView lv = (ListView)findViewById(R.id.exerciseList);

            ArrayAdapter<ExEntry> arrayAdapter = new ArrayAdapter<ExEntry>(this, R.layout.listviewfontsize, exEntryList);
            lv.setAdapter(arrayAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String excerciseName = ((TextView) view).getText().toString();
                    final Intent g = new Intent(addExercise.this, addExercise.class);
                    g.putExtra("workout", workoutName);

                    excerciseDbAdapter.deleteExEntry(workoutName,todaysDate,email,excerciseName);
                    startActivity(g);
                }
            });
        }
    }


    public List<ExEntry> getExcercisesForWorkout(String workoutName, String todaysDate, String email){

        List<ExEntry> exEntries = new ArrayList<>();
        String query = "SELECT reps, sets, exName, exWeight FROM exEntry ex, excercise e, workout w WHERE ex.exName = e.name AND ex.exWorkout = w.workName AND ex.exWorkout = \""+workoutName+"\" " +
                "AND ex.exDate = w.date AND ex.exDate = \""+todaysDate+"\" AND ex.exUName = w.uName AND ex.exUName = \""+email+"\" ;";
        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                ExEntry ex = new ExEntry();
                ex.setReps(c.getInt(c.getColumnIndex(excerciseDbAdapter.EXENTRY_REPS)));
                ex.setSets(c.getInt(c.getColumnIndex(excerciseDbAdapter.EXENTRY_SETS)));
                ex.setExName(c.getString(c.getColumnIndex(excerciseDbAdapter.EXENTRY_NAME)));
                ex.setWeight(c.getDouble(c.getColumnIndex(excerciseDbAdapter.EXENTRY_WEIGHT)));
                exEntries.add(ex);
            }while(c.moveToNext());

        }
        return exEntries;
    }

    public void addExcerciseClicked(View v){

        LayoutInflater li = LayoutInflater.from(context);
        View promptView = li.inflate(R.layout.repsetprompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final EditText inputSets = (EditText) promptView.findViewById(R.id.inputSets);
        final EditText inputReps = (EditText) promptView.findViewById(R.id.inputReps);
        final EditText inputWeight = (EditText)promptView.findViewById(R.id.inputWeight);
//----------------------------popup to allow user to input reps sets weight----------------------------------------------
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int d) {
                int sets = Integer.valueOf(String.valueOf(inputSets.getText()));
                int reps = Integer.valueOf(String.valueOf(inputReps.getText()));
                double weight = Double.valueOf(String.valueOf(inputWeight.getText()));
                EditText excercise = (EditText) findViewById(R.id.excerciseName);
                String excercisestr = excercise.getText().toString();

                ExEntry ex = new ExEntry();
                ex.setExName(excercisestr);
                ex.setReps(reps);
                ex.setSets(sets);
                ex.setWeight(weight);
                ex.setuName(email);
                ex.setExWorkout(workoutName);
                ex.setDate(todaysDate);

                excerciseDbAdapter.insertExEntry(ex);
                new updateExerciseEntry().execute();
                Excercise e = new Excercise();
                e.setExcerciseName(excercisestr);
                excerciseDbAdapter.insertExcercise(e);
                new updateExercise().execute();


                /*List<ExEntry> exEntryList = getExcercisesForWorkout(workoutName, todaysDate, email);
                ListView lv = (ListView) findViewById(R.id.excerciseList);
                //display excercises in workout in listView
                ArrayAdapter<ExEntry> arrayAdapter = new ArrayAdapter<ExEntry>(context, android.R.layout.simple_list_item_1, exEntryList);
                lv.setAdapter(arrayAdapter);*/

                Toast.makeText(addExercise.this, "Excercise added to workout", Toast.LENGTH_SHORT).show();
                Intent x = new Intent(addExercise.this, addExercise.class);
                x.putExtra("workout",workoutName);
                startActivity(x);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Intent x = new Intent(addExercise.this, addExercise.class);
                x.getStringExtra("workout");
                x.putExtra("workout", workoutName);
                startActivity(x);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//---------------------------------------------------------------------------------------------------------------------
    public void onBackPressed(){
        Intent i = new Intent(addExercise.this, addWorkout.class);
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
        Intent i = new Intent(addExercise.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(addExercise.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(addExercise.this, MainActivity_Homepage.class);
        startActivity(y);
    }
    private class updateExerciseEntry extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`exEntry` (`exNum`, `exWorkout`, `reps`, `sets`, `exName`, `exUName`, `exWeight`, `exDate`)";
        int sets,reps;
        double weight;
        String exercisestr;

        protected void onPreExecute(){

            EditText excercise = (EditText) findViewById(R.id.excerciseName);
            exercisestr = excercise.getText().toString();

        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('1', '"+workoutName+"', '"+reps+"', '"+sets+"', '"+exercisestr+"', '"+email+"', '"+weight+"', '"+todaysDate+"');");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class updateExercise extends AsyncTask<Void, Void, Void> {
        private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355?user=mydb1355mn&password=qa0tol";
        private Connection connection = null;
        private Statement statement =  null;
        private String sql= "INSERT INTO `mydb1355`.`excercise` (`name`)";

        String excercisestr;

        protected void onPreExecute(){
            EditText excercise = (EditText) findViewById(R.id.excerciseName);
            excercisestr = excercise.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(connectionString);
                statement = connection.createStatement();
                statement.executeUpdate(sql+"VALUES ('"+excercisestr+"')");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

