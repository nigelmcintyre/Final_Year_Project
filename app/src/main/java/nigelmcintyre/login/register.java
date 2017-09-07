package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class register extends Activity {

    SessionManager session;
    userDbAdapter helper = new userDbAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new SessionManager(getApplicationContext());
    }

    public void onSignUpClick(View v){
        if(v.getId() == R.id.btnRegister){
            EditText name = (EditText)findViewById(R.id.inputFirstName);
            EditText surname = (EditText)findViewById(R.id.inputSurname);
            EditText email = (EditText)findViewById(R.id.inputEmail);
            EditText password = (EditText)findViewById(R.id.inputPassword);
            EditText retypPassword = (EditText)findViewById(R.id.inputRetypePassword);

            String namestr = name.getText().toString();
            String surnamestr = surname.getText().toString();
            String emailstr = email.getText().toString();
            String passwordstr = password.getText().toString();
            String retypPasswordstr = retypPassword.getText().toString();

            if(!passwordstr.equals(retypPasswordstr))
            {
                //pop up message
                Toast pass = Toast.makeText(register.this, "Passwords don't match ", Toast.LENGTH_SHORT);
                pass.show();
            }
            //check if
            /*else if(passwordstr.length()<8||retypPasswordstr.length()<8){
                Toast pass = Toast.makeText(register.this, "Passwords too short ", Toast.LENGTH_SHORT);
                pass.show();
            }*/
            else{
                //insert details in database
                User registereUser = new User();
                registereUser.setName(namestr);
                registereUser.setSurname(surnamestr);
                registereUser.setEmail(emailstr);
                registereUser.setPassword(passwordstr);
                registereUser.setSex("");
                registereUser.setActivityLevel(0.0);

                helper.insertUser(registereUser);
                //allows email to be used on


                Intent i = new Intent(register.this, calculateBMR.class);
                session.createLoginSession(emailstr,passwordstr);
                i.putExtra("Email",emailstr);
                startActivity(i);
            }
        }

    }
}
