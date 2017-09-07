package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class editFood extends Activity {

    Intent i;
    String mealName;
    foodEntryDbAdapter dbAdapter = new foodEntryDbAdapter(this);
    SessionManager session;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        i = getIntent();
        session = new SessionManager(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        mealName = i.getStringExtra("mealName");

    }
    public void onEditFoodClicked(View v) {


        EditText name = (EditText) findViewById(R.id.inputEditFoodName);
        EditText carbs = (EditText) findViewById(R.id.inputEditFoodCarbs);
        EditText fats = (EditText) findViewById(R.id.inputEditFoodFats);
        EditText protein = (EditText) findViewById(R.id.inputFoodEditProtein);

        String namestr = name.getText().toString();
        double carbsstr = Double.parseDouble(carbs.getText().toString());
        double fatsstr = Double.parseDouble(fats.getText().toString());
        double proteinstr = Double.parseDouble(protein.getText().toString());

        Foods f = new Foods();
        double calories = ((carbsstr * 4) + (fatsstr * 9) + (proteinstr * 4));


        f.setCarbs(carbsstr);
        f.setProtein(proteinstr);
        f.setFat(fatsstr);
        f.setFoodName(namestr);
        f.setCalories(calories);
        dbAdapter.updateFood(f);

        i = new Intent(editFood.this, addFood.class);

        i.putExtra("mealName", mealName);
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
        Intent i = new Intent(editFood.this, calculateBMR.class);
        startActivity(i);

    }
    private void logoutPressed(){

        session.logoutUser();
        Intent x = new Intent(editFood.this, Login.class);
        startActivity(x);
    }
    private void homepage(){
        Intent y = new Intent(editFood.this, MainActivity_Homepage.class);
        startActivity(y);
    }
}
