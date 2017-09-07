package nigelmcintyre.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class loggedIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        String email = getIntent().getStringExtra("Email");

        TextView tv = (TextView)findViewById(R.id.inputEmail);
        tv.setText(email);
    }
}
