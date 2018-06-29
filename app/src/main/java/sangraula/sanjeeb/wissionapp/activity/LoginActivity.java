package sangraula.sanjeeb.wissionapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordET;
    private AutoCompleteTextView mEmailATV;
    private Button mLoginButton, mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailATV = findViewById(R.id.email_atv);
        mPasswordET = findViewById(R.id.password_et);

        mLoginButton = findViewById(R.id.login_button);
        mRegisterButton = findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    submitForm();
                } else {
                    Toast.makeText(LoginActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            Toast.makeText(this, "You're already logged in", Toast.LENGTH_SHORT).show();
            mLoginButton.setEnabled(false);

        } else {

            mLoginButton.setEnabled(true);
        }

    }

    private void submitForm() {

        String email, password, confirmPassword;

        email = mEmailATV.getText().toString();
        password = mPasswordET.getText().toString();

        if (isEmpty(email)) {

            mEmailATV.setError("Enter Email");
        } else if (isEmpty(password)) {

            mPasswordET.setError("Enter Password");
        } else {

            handleLogin(email, password);

        }
    }


    private void handleLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, "You're logged in", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(LoginActivity.this, "Failed to log you in", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private boolean isEmpty(String s) {

        return ValidationUtils.StringValidation.isEmpty(s);
    }
}
