package sangraula.sanjeeb.wissionapp.activity;

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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mPasswordET, mConfirmPasswordET;
    private AutoCompleteTextView mEmailATV;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailATV = findViewById(R.id.email_atv);
        mPasswordET = findViewById(R.id.password_et);
        mConfirmPasswordET = findViewById(R.id.confirm_password_et);

        mRegisterButton = findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = mAuth.getCurrentUser();

                if (user == null) {
                    submitForm();
                } else {
                    Toast.makeText(RegisterActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();
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
            mRegisterButton.setEnabled(false);

        } else {

            mRegisterButton.setEnabled(true);
        }

    }

    private void submitForm() {

        String email, password, confirmPassword;

        email = mEmailATV.getText().toString();
        password = mPasswordET.getText().toString();
        confirmPassword = mConfirmPasswordET.getText().toString();

        if (isEmpty(email)) {

            mEmailATV.setError("Enter Email");
        } else if (isEmpty(password)) {

            mPasswordET.setError("Enter Password");
        } else if (isEmpty(confirmPassword)) {

            mConfirmPasswordET.setError("Confirm your password");
        } else if (!password.equals(confirmPassword)) {

            mPasswordET.setError("Passwords don't match");
        } else {

            handleRegister(email, password);

        }
    }


    private void handleRegister(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(RegisterActivity.this, "You're registered", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(RegisterActivity.this, "Failed to register you", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private boolean isEmpty(String s) {

        return ValidationUtils.StringValidation.isEmpty(s);
    }
}
