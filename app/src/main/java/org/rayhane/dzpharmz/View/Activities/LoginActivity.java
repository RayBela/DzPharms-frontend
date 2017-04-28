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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText userEmail ;
    private EditText userPassword;
    private Button loginBtn;
    private TextView createAccountLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (EditText)findViewById(R.id.user_email);
        userPassword = (EditText)findViewById(R.id.user_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        createAccountLink = (TextView)findViewById(R.id.create_account_label);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });



    }

    public void goSignupActivity(View view){

        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        //startActivityForResult(intent, REQUEST_SIGNUP);
        //intent.putExtra(REQUEST_SIGNUP, );
        startActivity(intent);
    }


    /**
     * method for login
     */

    private void login (){
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.MyProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authentification...");
        progressDialog.show();

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        // ajouter code d'authentification

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // soit onLoginSuccess ou onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                //implementer  l'inscription
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Erreur", Toast.LENGTH_LONG).show();

        loginBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("entrer une adresse e-mail valide");
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
