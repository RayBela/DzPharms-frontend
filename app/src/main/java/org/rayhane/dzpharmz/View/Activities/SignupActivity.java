package org.rayhane.dzpharmz.View.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.rayhane.dzpharmz.R;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText userEmail ;
    private EditText userPassword ;
    private EditText userName ;
    private Button signupBtn;

   /* public void goLoginActivity(View view){

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //startActivityForResult(intent, REQUEST_SIGNUP);
        //intent.putExtra(REQUEST_SIGNUP, );
        startActivity(intent);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userEmail = (EditText)findViewById(R.id.email_text);
        userPassword = (EditText)findViewById(R.id.password_text);
        userName = (EditText)findViewById(R.id.user_text);
        TextView loginLink = (TextView) findViewById(R.id.login_link);
        signupBtn = (Button) findViewById(R.id.signup_btn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                //intent.putExtra(REQUEST_SIGNUP, );
                startActivity(intent);
            }
        });


    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.MyProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creation d'un compte...");
        progressDialog.show();

        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            userName.setError("au moins 3 caractéres");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("entrer une adresse mail valide");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            userPassword.setError("entre 6 et 12 caractéres");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        return valid;
    }

}
