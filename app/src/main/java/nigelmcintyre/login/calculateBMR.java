package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static java.lang.StrictMath.rint;

public class calculateBMR extends Activity {

    userDbAdapter helper = new userDbAdapter(this);
    SessionManager session;
    private RadioButton radioSexButton, radioActivityButton,radioGoalButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmr);
    }

    public void calBmr(View v){
        EditText age = (EditText)findViewById(R.id.inputAge);
        EditText height = (EditText)findViewById(R.id.inputHeight);
        EditText weight = (EditText)findViewById(R.id.inputWeight);
        RadioGroup radioSexGroup = (RadioGroup)findViewById(R.id.radioGroup);

        int sexSelectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton=(RadioButton)findViewById(sexSelectedId);

        String sexstr = radioSexButton.getText().toString();
        int agestr = Integer.parseInt(age.getText().toString());
        double heightstr = Double.parseDouble(height.getText().toString());
        double weightstr = Double.parseDouble(weight.getText().toString());

        TextView calcedBmr = (TextView) findViewById(R.id.tvBmr);


        double BMR = 0.0;

        //Toast.makeText(calculateBMR.this, (int) activityLevel,Toast.LENGTH_SHORT).show();
        if(sexstr.equals("Male"))
            BMR=rint((66)+(13.7*weightstr)+(5*heightstr)-(6.8*agestr));
        else
            BMR=rint(655+(9.6*weightstr)+(1.8*heightstr)-(4.7*agestr));
        calcedBmr.setText(String.valueOf(BMR));


    }

    public void calcButtonClick(View v){

            EditText age = (EditText)findViewById(R.id.inputAge);
            EditText height = (EditText)findViewById(R.id.inputHeight);
            EditText weight = (EditText)findViewById(R.id.inputWeight);
            RadioGroup radioSexGroup = (RadioGroup)findViewById(R.id.radioGroup);
            RadioGroup radioActiviyGroup = (RadioGroup)findViewById(R.id.activityGroup);
            RadioGroup radioGoalGroup = (RadioGroup)findViewById(R.id.goalGroup);
            EditText carbs= (EditText)findViewById(R.id.inputCarbs);
            EditText fats = (EditText)findViewById(R.id.inputFats);
            EditText protein = (EditText)findViewById(R.id.inputProtein);

            int sexSelectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton=(RadioButton)findViewById(sexSelectedId);

            int activitySelectedId = radioActiviyGroup.getCheckedRadioButtonId();
            radioActivityButton=(RadioButton)findViewById(activitySelectedId);

            int goalSelectedId = radioGoalGroup.getCheckedRadioButtonId();
            radioGoalButton = (RadioButton)findViewById(goalSelectedId);

            String activitystr = radioActivityButton.getText().toString();
            String goalstr = radioGoalButton.getText().toString();
            String sexstr = radioSexButton.getText().toString();
            int agestr = Integer.parseInt(age.getText().toString());
            double heightstr = Double.parseDouble(height.getText().toString());
            double weightstr = Double.parseDouble(weight.getText().toString());
            int carbstr = Integer.parseInt(carbs.getText().toString());
            int fatstr = Integer.parseInt(fats.getText().toString());
            int proteinstr = Integer.parseInt(protein.getText().toString());

            User u = new User();

            double activityLevel = 0.0;
            if(activitystr.equals("Sedentary")) {
                activityLevel = 1.2;
                u.setActivityLevel(activityLevel);
            }
            else if(activitystr.equals("Lightly Active")) {
                activityLevel = 1.375;
                u.setActivityLevel(activityLevel);
            }
            else if(activitystr.equals("Moderately Active")) {
                activityLevel = 1.55;
                u.setActivityLevel(activityLevel);
            }
            else if(activitystr.equals("Very Active")) {
                activityLevel = 1.725;
                u.setActivityLevel(activityLevel);
            }
            else{
                activityLevel = 1.9;
                u.setActivityLevel(activityLevel);
            }
            double BMR = 0.0;
            double TDEE =0.0;
            //Toast.makeText(calculateBMR.this, (int) activityLevel,Toast.LENGTH_SHORT).show();
            if(sexstr.equals("Male")) {
                BMR = ((66) + (13.7 * weightstr) + (5 * heightstr) - (6.8 * agestr));
                TDEE = BMR * activityLevel;
            }
            else{
                BMR=(655+(9.6*weightstr)+(1.8*heightstr)-(4.7*agestr));
                TDEE = BMR*activityLevel;
            }


            double goal = 0.0;
            if(goalstr.equals("Loose Weight")){
                goal = TDEE-(TDEE*0.20);
            }
            else if(goalstr.equals("Maintain weight")){
                goal = TDEE;
            }
            else{
                goal = TDEE+(TDEE*0.10);
            }
            double carbGram = 0.0;
            double fatGram = 0.0;
            double proteinGram = 0.0;
            if(carbstr+proteinstr+fatstr ==100){


                double carbCal = ((carbstr*0.01)*goal);
                double fatCal = ((fatstr*0.01)*goal);
                double proteinCal = ((proteinstr*0.01)*goal);

                carbGram = carbCal/4;
                fatGram = fatCal/9;
                proteinGram = proteinCal/4;

                session= new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(SessionManager.KEY_EMAIL);
                u = helper.getInfo(email);
                u.setSex(sexstr);
                u.setBMR(BMR);
                u.setTDEE(TDEE);
                u.setHeight(heightstr);
                u.setWeight(weightstr);
                u.setAge(agestr);
                u.setGoal(rint(goal));
                u.setCarbs(rint(carbGram));
                u.setFats(rint(fatGram));
                u.setProtein(rint(proteinGram));

                helper.updateCalUser(u);
                Intent i = new Intent(calculateBMR.this, MainActivity_Homepage.class);

                startActivity(i);
            }
            else{
                Toast.makeText(calculateBMR.this,"Percentage must add up 100%",Toast.LENGTH_SHORT).show();
            }




          //Toast.makeText(calculateBMR.this,radioActivityButton.getText(),Toast.LENGTH_SHORT).show();



    }
}
