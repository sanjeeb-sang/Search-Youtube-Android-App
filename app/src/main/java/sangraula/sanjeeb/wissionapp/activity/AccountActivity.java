package sangraula.sanjeeb.wissionapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.data.FirebasePaths;
import sangraula.sanjeeb.wissionapp.models.firebase.UserData;

public class AccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private EditText mGenderET, mAgeET, mUsernameET;
    private ImageView mImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        mImageView = findViewById(R.id.image_view);
        Button changePicButton = findViewById(R.id.change_pic_button);
        Button saveButton = findViewById(R.id.save_button);
        mGenderET = findViewById(R.id.gender_et);
        Button changeUsernameButton = findViewById(R.id.username_button);
        mAgeET = findViewById(R.id.age_et);
        mUsernameET = findViewById(R.id.username_et);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveFields();
            }
        });

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeUsername();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        changePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser u = mAuth.getCurrentUser();

                if (u != null) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }

            }
        });
    }


    private void changeUsername () {

        String uName = mUsernameET.getText().toString();

        if (uName != null && uName.length() != 0) {

            if (!uName.equals(mAuth.getCurrentUser().getDisplayName())) {

                AccountHelper.updateUsername(this, uName, new AccountHelper.Callback() {
                    @Override
                    public void onSuccess(Task<Void> task) {

                        FirebaseUser u = mAuth.getCurrentUser();
                        String uName = u.getDisplayName();
                        mUsernameET.setText(uName);

                        Toast.makeText(AccountActivity.this, "Display Name Changed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Task<Void> task) {

                    }
                });
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        setValues();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_IMAGE:

                if (resultCode == RESULT_OK && data.getData() != null) {
                    AccountHelper.updateProfilePic(this, data.getData(), new AccountHelper.Callback() {
                        @Override
                        public void onSuccess(Task<Void> task) {

                            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(mImageView);
                        }

                        @Override
                        public void onFailure(Task<Void> task) {

                        }
                    });
                }

        }
    }


    private void setValues() {

        FirebaseUser u = mAuth.getCurrentUser();

        if (u != null) {

            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(mImageView);

            mUsernameET.setText(u.getDisplayName());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebasePaths.UserData.getUsersPath(mAuth.getCurrentUser()));

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserData data = dataSnapshot.getValue(UserData.class);

                    if (data != null) {
                        if (data.age != null) {
                            mAgeET.setText(data.age);
                        }
                        if (data.gender != null) {
                            mGenderET.setText(data.gender);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    private void saveFields() {

        FirebaseUser u = mAuth.getCurrentUser();

        if (u != null) {

            String gender = mGenderET.getText().toString();
            String age = mAgeET.getText().toString();

            Picasso.get().load(u.getPhotoUrl()).into(mImageView);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebasePaths.UserData.getUsersPath(mAuth.getCurrentUser()));

            UserData data = new UserData(mAuth.getCurrentUser().getUid(), gender, age);

            ref.updateChildren(data.toMap());

        }
    }


}
