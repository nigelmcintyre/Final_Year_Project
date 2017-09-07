package nigelmcintyre.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class Login extends Activity {

    SessionManager session;
    userDbAdapter helper = new userDbAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());

    }



    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnLogin) {

            EditText a = (EditText) findViewById(R.id.inputEmail);

            String str = a.getText().toString();
            EditText b = (EditText) findViewById(R.id.inputPassword);
            String pass = b.getText().toString();

            String password =  helper.searchPassword(str);
            if(pass.equals(password)){
                session.createLoginSession(str,pass);
                Intent i = new Intent(Login.this, MainActivity_Homepage.class);
                i.putExtra("Email",str);
                startActivity(i);
            }
            else{
                Toast temp = Toast.makeText(Login.this, "Email or username are incorrect ", Toast.LENGTH_SHORT);
                temp.show();
            }


           /* Intent i = new Intent(Login.this, loggedIn.class);
            i.putExtra("Email", str);
            startActivity(i);*/
        }
        if (v.getId() == R.id.btnSignUp) {
            Intent i = new Intent(Login.this, register.class);
            startActivity(i);
        }


    }
}
