package sangraula.sanjeeb.wissionapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.fragment.VideoListFragment;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View mHeaderView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateDrawerHeader(firebaseAuth.getCurrentUser());
            }
        };

        VideoListFragment fragment = new VideoListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        updateDrawerHeader(mAuth.getCurrentUser());
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mListener);

    }


    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mListener);
    }

    private void updateDrawerHeader(FirebaseUser user) {

        TextView emailTV = mHeaderView.findViewById(R.id.email_tv);
        TextView usernameTV = mHeaderView.findViewById(R.id.username_tv);
        ImageView profileIV = mHeaderView.findViewById(R.id.imageView);

        if (user != null) {
            // User is signed in

            String email = mAuth.getCurrentUser().getEmail();
            email = (email == null) ? "No email" : email;

            String username = mAuth.getCurrentUser().getDisplayName();
            username = (username == null || username.equals("")) ? "Name" : username;

            emailTV.setText(email);

            usernameTV.setText(username);

            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(profileIV);

        } else {
            // User is signed out

            emailTV.setText("");
            usernameTV.setText("");
            profileIV.setImageResource(0);

        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sign_out:

                FirebaseAuth.getInstance().signOut();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.login:

                Intent i = new Intent(ListActivity.this, LoginActivity.class);
                startActivity(i);

                break;

            case R.id.account:

                Intent i2 = new Intent(ListActivity.this, AccountActivity.class);
                startActivity(i2);

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
